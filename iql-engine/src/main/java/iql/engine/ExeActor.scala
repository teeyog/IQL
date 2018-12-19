package iql.engine

import java.io.{ByteArrayOutputStream, PrintStream}
import java.lang.reflect.Modifier

import akka.actor.{Actor, Props}
import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import iql.common.Logging
import iql.common.domain.Bean._
import iql.common.domain.{JobStatus, SQLMode}
import iql.common.utils.{ObjGenerator, ZkUtils}
import iql.engine.antlr.{IQLBaseListener, IQLLexer, IQLParser}
import iql.engine.main.IqlMain
import iql.engine.main.IqlMain.{warn, _}
import iql.engine.repl.Interpreter._
import iql.engine.repl.SparkInterpreter
import iql.engine.utils.{BatchSQLRunnerEngine, PropsUtils}
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.bridge.SparkBridge
import iql.engine.ExeActor._
import iql.engine.auth.{DataAuth, IQLAuthListener}
import iql.engine.config._
import org.I0Itec.zkclient.ZkClient
import org.apache.spark.sql.functions.col


class ExeActor(_interpreter: SparkInterpreter, iqlSession: IQLSession, conf: SparkConf) extends Actor with Logging {

    var sparkSession: SparkSession = _
    var interpreter: SparkInterpreter = _
    var zkClient: ZkClient = _
    var iqlExcution: IQLExcution = _
    val zkValidActorPath = ZkUtils.validEnginePath + "/" + iqlSession.engineInfo + "_" + context.self.path.name
    val initHiveCatalog = conf.getBoolean(INIT_HIVE_CATALOG.key, INIT_HIVE_CATALOG.defaultValue.get)
    val autoComplete = conf.getBoolean(HIVE_CATALOG_AUTO_COMPLETE.key, HIVE_CATALOG_AUTO_COMPLETE.defaultValue.get)
    val authEnable = conf.getBoolean(IQL_AUTH_ENABLE.key, IQL_AUTH_ENABLE.defaultValue.get)

    override def preStart(): Unit = {
        warn("Actor Start ...")
        zkClient = ZkUtils.getZkClient(PropsUtils.get("zkServers"))
        interpreter = _interpreter
        sparkSession = IqlMain.createSpark(conf).newSession()
        registerUDF("iql.engine.utils.SparkUDF") //注册常用UDF
        ZkUtils.registerActorInEngine(zkClient, zkValidActorPath, "", 6000, -1)
    }

    override def postStop(): Unit = {
        warn("Actor Stop ...")
        interpreter.close()
        sparkSession.stop()
    }

    override def receive: Receive = {

        case Iql(mode, iql, variables) =>
            actorWapper() { () => {
                var rIql = iql
                val variblesIters = JSON.parseArray(variables).iterator()
                while (variblesIters.hasNext) {
                    val nObject = JSON.parseObject(variblesIters.next().toString)
                    rIql = rIql.replace("${" + nObject.getString("name") + "}", nObject.getString("value"))
                }
                schedulerMode = !schedulerMode //切换调度池
                sparkSession.sparkContext.setLocalProperty("spark.scheduler.pool", if (schedulerMode) "pool_fair_1" else "pool_fair_2")
                iqlExcution = IQLExcution(iql = iql, variables = variables)
                //为当前iql设置groupId
                val groupId = BatchSQLRunnerEngine.getGroupId
                iqlExcution.engineInfoAndGroupId = iqlSession.engineInfo + "_" + groupId
                sparkSession.sparkContext.clearJobGroup()
                sparkSession.sparkContext.setJobDescription("iql:" + rIql)
                sparkSession.sparkContext.setJobGroup("iqlid:" + groupId, "iql:" + rIql)
                //将该iql任务的唯一标识返回
                sender() ! iqlExcution

                mode match {
                    case SQLMode.IQL=>
                        val execListener = new IQLSQLExecListener(sparkSession, iqlSession)
                        execListener.addAuthListener(if (authEnable) {
                            Some(new IQLAuthListener(sparkSession))
                        } else None)
                        parseSQL(rIql, execListener)
                        execListener.refreshTableAndView()
                    case SQLMode.CODE =>
                        warn("\n" + ("*" * 80) + "\n" + rIql + "\n" + ("*" * 80))
                        iqlExcution.mode = SQLMode.CODE
                        rIql = rIql.replaceAll("'", "\"").replaceAll("\n", " ")
                        val response = interpreter.execute(rIql)
                        getExecuteState(response)
                    case _ =>
                }
            }
            }

        case HiveCatalog() =>
            val catalog = initHiveCatalog match {
                case true => getHiveCatalog
                case false => "{}"
            }
            sender() ! catalog

        case HiveCatalogWithAutoComplete() =>
            val catalog = autoComplete match {
                case true => getHiveCatalogWithAutoComplete
                case false => "{}"
            }
            sender() ! catalog

        case HiveTables() => sender() ! hiveTables(sparkSession)

        case GetBatchResult(engineInfoAndGroupId) =>
            if (iqlSession.batchJob.keySet().contains(engineInfoAndGroupId)) {
                sender() ! iqlSession.batchJob.get(engineInfoAndGroupId)
                iqlSession.batchJob.remove(engineInfoAndGroupId)
            } else {
                sender() ! IQLExcution()
            }

        case GetActiveStream() =>
            sender() ! iqlSession.streamJob.filter(_._2.isActive).keys.foldLeft(new JSONArray()) {
                case (array, stream) =>
                    stream.split("_") match {
                        case Array(engineInfo, name, uid) =>
                            val obj = new JSONObject()
                            obj.put("engineInfo", engineInfo)
                            obj.put("name", name)
                            obj.put("uid", uid)
                            array.add(obj)
                    }
                    array
            }.toJSONString

        case StreamJobStatus(name) =>
            sender() ! iqlSession.streamJob(name).status.prettyJson

        case StopSreamJob(name) =>
            iqlSession.streamJob(name).stop()
            sender() ! !iqlSession.streamJob(name).isActive

        case CancelJob(groupId) =>
            sparkSession.sparkContext.cancelJobGroup("iqlid:" + groupId)
            sender() ! true

        case StopIQL() => context.system.terminate()

        case _ => None
    }

    // antlr4解析SQL语句
    def parseSQL(input: String, listener: IQLSQLExecListener): Unit = {
        try {
            parse(input, listener)
            iqlExcution.takeTime = System.currentTimeMillis() - iqlExcution.startTime.getTime
            val hdfsPath = "/tmp/iql/result/iql_query_result_" + System.currentTimeMillis()
            iqlExcution.hdfsPath = hdfsPath
            if(listener.result().containsKey("uuidTable")){
                val showTable = sparkSession.table(listener.getResult("uuidTable"))
                iqlExcution.data = showTable.limit(500).toJSON.collect().mkString("[",",","]")
                iqlExcution.schema = showTable.schema.fields.map(_.name).mkString(",")
                iqlExcution.status = JobStatus.FINISH
                iqlSession.batchJob.put(iqlExcution.engineInfoAndGroupId, iqlExcution)
                showTable.select(showTable.columns.map(col(_).cast("String")):_*).write.json(hdfsPath)
            } else if(listener.result().containsKey("explainStr")){
                iqlExcution.data = listener.getResult("explainStr")
                iqlExcution.dataType = "preData"
                iqlExcution.status = JobStatus.FINISH
                iqlSession.batchJob.put(iqlExcution.engineInfoAndGroupId, iqlExcution)
            }
        } catch {
            case e: Exception =>
                e.printStackTrace()
                iqlExcution.success = false
                val out = new ByteArrayOutputStream()
                e.printStackTrace(new PrintStream(out))
                iqlExcution.data = new String(out.toByteArray)
                iqlExcution.dataType = "errorData"
                out.close()
        }
        iqlExcution.status = JobStatus.FINISH
        iqlSession.batchJob.put(iqlExcution.engineInfoAndGroupId, iqlExcution)
    }

    // 执行前从zk中删除当前对应节点（标记不可用），执行后往zk中写入可用节点（标记可用）
    def actorWapper()(f: () => Unit) {
        ZkUtils.deletePath(zkClient, zkValidActorPath)
        try {
            f()
        } catch {
            case e: Exception =>
                val out = new ByteArrayOutputStream()
                e.printStackTrace(new PrintStream(out))
                iqlExcution.data = new String(out.toByteArray)
                iqlExcution.dataType = "errorData"
                sender() ! iqlExcution
        }
        ZkUtils.registerActorInEngine(zkClient, zkValidActorPath, "", 6000, -1)
    }

    // 获取hive元数据信息
    def getHiveCatalog: String = {
        val hiveArray = new JSONArray()
        var num: Int = 0
        SparkBridge.getHiveCatalg(sparkSession).client.listDatabases("*").foreach(db => {
            num += 1
            val dbId = num
            val dbObj = new JSONObject()
            dbObj.put("id", dbId)
            dbObj.put("name", db)
            dbObj.put("pId", 0)
            hiveArray.add(dbObj)
            SparkBridge.getHiveCatalg(sparkSession).client.listTables(db).foreach(tb => {
                num += 1
                val tbId = num
                val tbObj = new JSONObject()
                tbObj.put("id", tbId)
                tbObj.put("pId", dbId)
                tbObj.put("name", tb)
                hiveArray.add(tbObj)
                SparkBridge.getHiveCatalg(sparkSession).client.getTable(db, tb).schema.fields.foreach(f => {
                    num += 1
                    val fieldId = num
                    val fieldObj = new JSONObject()
                    fieldObj.put("id", fieldId)
                    fieldObj.put("pId", tbId)
                    fieldObj.put("name", f.name + "(" + f.dataType.typeName + ")")
                    hiveArray.add(fieldObj)
                }
                )
            })
        })
        hiveArray.toJSONString
    }

    def getHiveCatalogWithAutoComplete: String = {
        val hiveObj = new JSONObject()
        SparkBridge.getHiveCatalg(sparkSession).client.listDatabases("*").foreach(db => {
            val tbArray = new JSONArray()
            SparkBridge.getHiveCatalg(sparkSession).client.listTables(db).foreach(tb => {
                tbArray.add(tb)
                val cloArray = new JSONArray()
                SparkBridge.getHiveCatalg(sparkSession).client.getTable(db, tb).schema.fields.foreach(f => cloArray.add(f.name))
                hiveObj.put(tb, cloArray)
            })
            hiveObj.put(db, tbArray)
        })
        hiveObj.toJSONString
    }

    def getExecuteState(response: ExecuteResponse): Unit = {
        response match {
            case _: ExecuteIncomplete => getExecuteState(response)
            case e: ExecuteSuccess =>
                val take = (System.currentTimeMillis() - iqlExcution.startTime.getTime) / 1000
                iqlExcution.takeTime = take
                iqlExcution.data = e.content.values.values.mkString("\n")
                iqlExcution.dataType = "preData"
                iqlExcution.status = JobStatus.FINISH
                iqlSession.batchJob.put(iqlExcution.engineInfoAndGroupId, iqlExcution)
            case e: ExecuteError =>
                iqlExcution.status = JobStatus.FINISH
                iqlExcution.data = e.evalue
                iqlExcution.success = false
                iqlExcution.dataType = "errorData"
                iqlSession.batchJob.put(iqlExcution.engineInfoAndGroupId, iqlExcution)
            case e: ExecuteAborted =>
                iqlExcution.status = JobStatus.FINISH
                iqlExcution.data = e.message
                iqlExcution.success = false
                iqlExcution.dataType = "errorData"
                iqlSession.batchJob.put(iqlExcution.engineInfoAndGroupId, iqlExcution)
            case _ =>
        }
    }

    def registerUDF(clazz: String): Unit = {
        Class.forName(clazz).getMethods.foreach { f =>
            try {
                if (Modifier.isStatic(f.getModifiers)) {
                    f.invoke(null, sparkSession)
                }
            } catch {
                case e: Exception => e.printStackTrace()
            }
        }
    }
}

object ExeActor {

    def props(interpreter: SparkInterpreter, iqlSession: IQLSession, sparkConf: SparkConf): Props = Props(new ExeActor(interpreter, iqlSession, sparkConf))

    // antlr4解析SQL语句
    def parseStr(input: String, listener: IQLBaseListener): Unit = {
        val loadLexer = new IQLLexer(new ANTLRInputStream(input))
        val tokens = new CommonTokenStream(loadLexer)
        val parser = new IQLParser(tokens)
        val stat = parser.statement()
        ParseTreeWalker.DEFAULT.walk(listener, stat)
    }

    // 权限验证
    def checkAuth(input: String, authListener: Option[IQLAuthListener]) = {
        authListener.foreach(parseStr(input, _))
    }

    def parse(input: String, execListener: IQLSQLExecListener, fromImport: Boolean = false): Unit = {
        warn("\n" + ("*" * 80) + "\n" + input + "\n" + ("*" * 80))
        if (!fromImport) {
            checkAuth(input, execListener.authListener())
            execListener.authListener().foreach(listener => {
                val authResults = DataAuth.auth(listener.tables().tables.toList).filter(!_.granted)
                if(authResults.nonEmpty){
                    throw new Exception(authResults.map(_.msg).mkString("\n"))
                }
            })
        }
        parseStr(input, execListener)
    }

    /**
      *  get hive tables
      */
    def hiveTables(spark:SparkSession) = {
        val tableArray = new JSONArray()
        SparkBridge.getHiveCatalg(spark).client.listDatabases("*").foreach(db => {
            SparkBridge.getHiveCatalg(spark).client.listTables(db).foreach(tb => {
                tableArray.add(ObjGenerator.newJSON(Seq(("type","hive"),("db",db),("table",tb)):_*))
            })
        })
        tableArray.toJSONString
    }

}
