package cn.i4.iql

import java.io.{ByteArrayOutputStream, PrintStream}
import java.sql.Timestamp
import java.util.concurrent.ConcurrentHashMap

import akka.actor.{Actor, ActorLogging, Props}
import cn.i4.iql.antlr.{IQLLexer, IQLListener, IQLParser}
import cn.i4.iql.domain.Bean._
import cn.i4.iql.utils.BatchSQLRunnerEngine
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import org.apache.spark.sql.SparkSession
import cn.i4.iql.repl.SparkInterpreter
import com.alibaba.fastjson.{JSONArray, JSONObject}
import cn.i4.iql.IqlService._
import org.apache.spark.sql.bridge.SparkBridge


class ExeActor(spark: SparkSession) extends Actor with ActorLogging {

  var sparkSession: SparkSession = _
  var interpreter: SparkInterpreter = _
  var resultMap = new ConcurrentHashMap[String, String]()
  val resJson = new JSONObject()

  override def preStart(): Unit = {
    log.info("Actor Start ...")
    sparkSession = spark.newSession()
    interpreter = new SparkInterpreter(sparkSession)
    interpreter.start()
    Class.forName("cn.i4.iql.utils.SparkUDF").getMethods.filter(_.getModifiers == 9).foreach { f =>
      f.invoke(null, sparkSession)
    }
  }

  override def postStop(): Unit = {
    log.info("Actor Stop ...")
    interpreter.close()
    sparkSession.stop()
  }

  override def receive: Receive = {

    case ShakeHands() => sender() ! ShakeHands()

    case HiveCatalog() =>
      val hiveArray = new JSONArray()
      var num:Int = 0
      SparkBridge.getHiveCatalg(sparkSession).client.listDatabases("*").foreach(db => {
        num += 1
        val dbId = num
        val dbObj = new JSONObject()
        dbObj.put("id", dbId)
        dbObj.put("name",s"""<span class="button ico_close" style="background:url('iql/img/db.jpg') center center/15px 15px no-repeat"></span>$db""")
        dbObj.put("pId",0)
        hiveArray.add(dbObj)
        SparkBridge.getHiveCatalg(sparkSession).client.listTables(db).foreach(tb => {
          num += 1
          val tbId = num
          val tbObj = new JSONObject()
          tbObj.put("id", tbId)
          tbObj.put("pId",dbId)
          tbObj.put("name",s"""<span class="button ico_close" style="background:url('iql/img/tb.jpg') center center/15px 15px no-repeat"></span>$tb""")
          hiveArray.add(tbObj)
          SparkBridge.getHiveCatalg(sparkSession).client.getTable(db, tb).schema.fields.foreach(f => {
            num += 1
            val fieldId = num
            val fieldObj = new JSONObject()
            fieldObj.put("id", fieldId)
            fieldObj.put("pId",tbId)
            fieldObj.put("name",f.name + "(" + f.dataType.typeName + ")")
            hiveArray.add(fieldObj)
          }
          )
        })
      })
      sender() ! hiveArray.toJSONString

    case Iql(code, iql, engineId) =>
      schedulerMode = !schedulerMode
      sparkSession.sparkContext.setLocalProperty("spark.scheduler.pool", if (schedulerMode) "pool_fair_1" else "pool_fair_2")
      resultMap.clear()
      resJson.clear()
      resJson.put("startTime", new Timestamp(System.currentTimeMillis))
      resJson.put("iql", iql)
      resJson.put("code", code)
      if (!code.trim.equals("")) interpreter.execute(code.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "")) //过滤掉注释
    //为当前iql设置groupId
    val groupId = BatchSQLRunnerEngine.getGroupId
      resJson.put("engineIdAndGroupId", engineId + ":" + groupId)
      sparkSession.sparkContext.clearJobGroup()
      sparkSession.sparkContext.setJobDescription("iql:" + iql)
      sparkSession.sparkContext.setJobGroup("iqlid:" + groupId, "iql:" + iql)
      //将该iql任务的唯一标识返回
      sender() ! resJson.toJSONString
      //解析iql并执行
      parse(iql.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", ""),
        new IQLSQLExecListener(sparkSession, null, resultMap))

    case GetResult(engineIdAndGroupId) =>
      if (jobMap.keySet().contains(engineIdAndGroupId)) {
        sender() ! jobMap.get(engineIdAndGroupId)
        jobMap.remove(engineIdAndGroupId)
      } else {
        sender() ! "{'status':'RUNNING'}"
      }

    case CancelJob(groupId) =>
      sparkSession.sparkContext.cancelJobGroup("iqlid:" + groupId)

    case StopIQL() => context.system.terminate()

    case _ => None
  }

  def parse(input: String, listener: IQLListener) = {
    resJson.put("status", "FINISH")
    try {
      val loadLexer = new IQLLexer(new ANTLRInputStream(input))
      val tokens = new CommonTokenStream(loadLexer)
      val parser = new IQLParser(tokens)
      val stat = parser.statement()
      ParseTreeWalker.DEFAULT.walk(listener, stat)
      val endTime = System.currentTimeMillis()
      val take = (endTime - resJson.getTimestamp("startTime").getTime) / 1000
      resJson.put("hdfsPath", resultMap.getOrDefault("hdfsPath", ""))
      resJson.put("schema", resultMap.getOrDefault("schema", ""))
      resJson.put("takeTime", take)
      resJson.put("isSuccess", true)
    } catch {
      case e: Exception => {
        e.printStackTrace()
        resJson.put("isSuccess", false)
        val out = new ByteArrayOutputStream()
        e.printStackTrace(new PrintStream(out))
        resJson.put("errorMessage", new String(out.toByteArray))
        out.close()
      }
    }
    jobMap.put(resJson.getString("engineIdAndGroupId"), resJson.toJSONString)
  }
}

object ExeActor {

  def props(spark: SparkSession): Props = Props(new ExeActor(spark))

}
