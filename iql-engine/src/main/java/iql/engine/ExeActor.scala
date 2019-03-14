package iql.engine

import java.io.{ByteArrayOutputStream, PrintStream}
import java.lang.reflect.Modifier

import akka.actor.{Actor, Props}
import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}
import iql.common.Logging
import iql.common.domain.Bean._
import iql.common.domain.{JobStatus, ResultDataType, SQLMode}
import iql.common.utils.{MailUtils, ObjGenerator, ZkUtils}
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
import iql.spark.listener.IQLStreamingQueryListener
import org.I0Itec.zkclient.ZkClient
import org.apache.http.client.fluent.Request
import org.apache.http.entity.ContentType
import org.apache.spark.sql.execution.streaming.StreamingQueryWrapper
import org.apache.spark.sql.functions.col
import org.apache.spark.sql.streaming.StreamingQueryListener


class ExeActor(_interpreter: SparkInterpreter, iqlSession: IQLSession, conf: SparkConf) extends Actor with Logging {

    var sparkSession: SparkSession = _
    var interpreter: SparkInterpreter = _
    var zkClient: ZkClient = _
    var iqlExcution: IQLExcution = _
    val zkValidActorPath = ZkUtils.validEnginePath + "/" + iqlSession.engineInfo + "_" + context.self.path.name
    val initHiveCatalog = conf.getBoolean(INIT_HIVE_CATALOG.key, INIT_HIVE_CATALOG.defaultValue.get)
    val autoComplete = conf.getBoolean(HIVE_CATALOG_AUTO_COMPLETE.key, HIVE_CATALOG_AUTO_COMPLETE.defaultValue.get)
    val authEnable = conf.getBoolean(IQL_AUTH_ENABLE.key, IQL_AUTH_ENABLE.defaultValue.get)
    val mailEnable = conf.getBoolean(MAIL_ENABLE.key, MAIL_ENABLE.defaultValue.get)
    val streamJobMaxAttempts = conf.getInt(STREAMJOB_MAXATTRPTS.key, STREAMJOB_MAXATTRPTS.defaultValue.get)

    override def preStart(): Unit = {
        warn("Actor Start ...")
        zkClient = ZkUtils.getZkClient(PropsUtils.get("zkServers"))
        interpreter = _interpreter
        sparkSession = IqlMain.createSpark(conf).newSession()
        if (mailEnable) addListener
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
                sparkSession.sparkContext.setLocalProperty("spark.scheduler.pool",
                    if (schedulerMode) "pool_fair_1" else "pool_fair_2")
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
                    case SQLMode.IQL =>
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

        case GetJobIdsForGroup(engineInfoAndGroupId) =>
            val applicationId = sparkSession.sparkContext.applicationId
            val jobIds = sparkSession.sparkContext.statusTracker
              .getJobIdsForGroup(engineInfoAndGroupId.split("_")(1)).mkString(",")
            sender() ! ObjGenerator.newJSON(Seq(("applicationId",applicationId),("jobIds",jobIds)):_*)

        case GetActiveStream() =>
            sender() ! iqlSession.streamJob.filter(_._2.isActive).keys.foldLeft(new JSONArray()) {
                case (array, stream) =>
                    stream.split("_") match {
                        case Array(engineInfo, name, uid) =>
                            array.add(ObjGenerator.newJSON(Seq(("engineInfo", engineInfo), ("name", name), ("uid", uid)): _*))
                    }
                    array
            }.toJSONString

        case StreamJobStatus(name) =>
            sender() ! iqlSession.streamJob(name).status.prettyJson

        case StopSreamJob(name) =>
            iqlSession.streamJob(name).stop()
            sender() ! !iqlSession.streamJob(name).isActive
            iqlSession.streamJob.remove(name)
            iqlSession.streamJobMaxAttempts.remove(name)

        case CancelJob(groupId) =>
            sparkSession.sparkContext.cancelJobGroup("iqlid:" + groupId)
            sender() ! true

        case StopIQL() => context.system.terminate()

        case _ => None
    }

    def addListener = {
        val props = ObjGenerator.newProperties(Seq(("mail.smtp.auth", PropsUtils.get("mail.smtp.auth")),
            ("mail.smtp.host", PropsUtils.get("mail.smtp.host")), ("mail.smtp.port", PropsUtils.get("mail.smtp.port")),
            ("mail.user", PropsUtils.get("mail.user")), ("mail.password", PropsUtils.get("mail.password"))): _*)
        val handleFunc = (start: StreamingQueryListener.QueryStartedEvent,
                          end: StreamingQueryListener.QueryTerminatedEvent) => {
            val streamName = start.name
            val otherMsg =
                if (iqlSession.streamJobMaxAttempts.containsKey(streamName)
                    && iqlSession.streamJobMaxAttempts.get(streamName) > 0) {
                    val restTimes = streamJobMaxAttempts - iqlSession.streamJobMaxAttempts.get(streamName) + 1
                    if (restTimes <= streamJobMaxAttempts) Some(s"""正在尝试第${restTimes}次重启""")
                    else None
                } else None
            val receiver = iqlSession.streamJobWithMailReceiver.get(streamName)
            try {
                if (null != receiver) {
                    MailUtils.sendMail(props, Array(receiver, "IQL任务告警",
                        s"实时任务：$streamName(${start.id}) 执行失败...\n${otherMsg.getOrElse("")}\n ${end.exception.getOrElse("")}"))
                }
            } catch {
                case e: Exception => error("发送邮件失败...\n" + e)
            }
            if (iqlSession.streamJobWithDingDingReceiver.contains(streamName)) {
                try {
                    Request.Post(s"https://oapi.dingtalk.com/robot/send?access_token=${PropsUtils.get("access_token")}")
                        .bodyString(
                            s"""
                               |{
                               |     "msgtype": "markdown",
                               |     "markdown": {"title":"IQL任务告警",
                               |     "text":"### IQL任务告警  \n > 实时任务：$streamName（${start.id}）执行失败...\n
                               |     ${otherMsg.getOrElse("")}\n ${end.exception.getOrElse("")}"
                               |     }
                               | }
                        """.stripMargin, ContentType.APPLICATION_JSON)
                        .execute().returnContent().asString()
                } catch {
                    case e: Exception => error("发送钉钉失败...\n" + e)
                }
            }
            if (iqlSession.streamJobMaxAttempts.containsKey(streamName) && iqlSession.streamJobMaxAttempts.get(streamName) > 0) {
                val newQuery = iqlSession.streamJobWithDataFrame.get(streamName).start()
                iqlSession.streamJob.put(iqlSession.engineInfo + "_" + newQuery.name + "_" + newQuery.id, newQuery)
                iqlSession.streamJobMaxAttempts.put(streamName, iqlSession.streamJobMaxAttempts.get(streamName) - 1)
            } else {
                iqlSession.streamJobWithDataFrame.remove(streamName)
                iqlSession.streamJobMaxAttempts.remove(streamName)
                iqlSession.streamJobWithMailReceiver.remove(streamName)
                iqlSession.streamJobWithDingDingReceiver -= streamName
            }
        }
        sparkSession.streams.addListener(new IQLStreamingQueryListener(handleFunc))
    }

    // antlr4解析SQL语句
    def parseSQL(input: String, listener: IQLSQLExecListener): Unit = {
        try {
            parse(input, listener)
            iqlExcution.takeTime = System.currentTimeMillis() - iqlExcution.startTime.getTime
            val hdfsPath = "/tmp/iql/result/iql_query_result_" + System.currentTimeMillis()
            iqlExcution.hdfsPath = hdfsPath
            if (listener.result().containsKey("uuidTable")) {
                val showTable = sparkSession.table(listener.getResult("uuidTable"))
                iqlExcution.data = showTable.limit(500).toJSON.collect().mkString("[", ",", "]")
                iqlExcution.schema = showTable.schema.fields.map(_.name).mkString(",")
                iqlExcution.status = JobStatus.FINISH
                iqlSession.batchJob.put(iqlExcution.engineInfoAndGroupId, iqlExcution)
                showTable.select(showTable.columns.map(col(_).cast("String")): _*).write.json(hdfsPath)
            } else if (listener.result().containsKey("explainStr")) {
                iqlExcution.data = listener.getResult("explainStr")
                iqlExcution.dataType = ResultDataType.PRE_DATA
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
                iqlExcution.dataType = ResultDataType.ERROR_DATA
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
                iqlExcution.dataType = ResultDataType.ERROR_DATA
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
            hiveArray.add(ObjGenerator.newJSON(Seq(("id", dbId), ("name", db), ("pId", 0)): _*))
            SparkBridge.getHiveCatalg(sparkSession).client.listTables(db).foreach(tb => {
                num += 1
                val tbId = num
                hiveArray.add(ObjGenerator.newJSON(Seq(("id", tbId), ("pId", dbId), ("name", tb)): _*))
                SparkBridge.getHiveCatalg(sparkSession).client.getTable(db, tb).schema.fields.foreach(f => {
                    num += 1
                    val fieldId = num
                    hiveArray.add(ObjGenerator.newJSON(Seq(("id", fieldId), ("pId", tbId), ("name", f.name + "(" + f.dataType.typeName + ")")): _*))
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
                iqlExcution.dataType = ResultDataType.PRE_DATA
                iqlExcution.status = JobStatus.FINISH
                iqlSession.batchJob.put(iqlExcution.engineInfoAndGroupId, iqlExcution)
            case e: ExecuteError =>
                iqlExcution.status = JobStatus.FINISH
                iqlExcution.data = e.evalue
                iqlExcution.success = false
                iqlExcution.dataType = ResultDataType.ERROR_DATA
                iqlSession.batchJob.put(iqlExcution.engineInfoAndGroupId, iqlExcution)
            case e: ExecuteAborted =>
                iqlExcution.status = JobStatus.FINISH
                iqlExcution.data = e.message
                iqlExcution.success = false
                iqlExcution.dataType = ResultDataType.ERROR_DATA
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
                if (authResults.nonEmpty) {
                    throw new Exception(authResults.map(_.msg).mkString("\n"))
                }
            })
        }
        parseStr(input, execListener)
    }

    /**
      * get hive tables
      */
    def hiveTables(spark: SparkSession) = {
        val tableArray = new JSONArray()
        SparkBridge.getHiveCatalg(spark).client.listDatabases("*").foreach(db => {
            SparkBridge.getHiveCatalg(spark).client.listTables(db).foreach(tb => {
                tableArray.add(ObjGenerator.newJSON(Seq(("type", "hive"), ("db", db), ("table", tb)): _*))
            })
        })
        tableArray.toJSONString
    }

}
