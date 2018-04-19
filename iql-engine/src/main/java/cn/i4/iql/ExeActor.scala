package cn.i4.iql

import java.io.{ByteArrayOutputStream, PrintStream}
import java.net.InetAddress
import java.sql.Timestamp
import java.util.concurrent.ConcurrentHashMap

import akka.actor.{Actor, ActorLogging, Address, Props}
import cn.i4.iql.antlr.{IQLLexer, IQLListener, IQLParser}
import cn.i4.iql.domain.Bean._
import cn.i4.iql.utils.BatchSQLRunnerEngine
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import org.apache.spark.sql.SparkSession
import cn.i4.iql.repl.SparkInterpreter
import com.alibaba.fastjson.JSONObject
import cn.i4.iql.IqlService._


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
      f.invoke(null,sparkSession)
    }
  }
  override def postStop(): Unit = {
    log.info("Actor Stop ...")
    interpreter.close()
    sparkSession.stop()
  }
  override def receive: Receive = {

    case ShakeHands() => sender() ! ShakeHands()

    case Iql(code,iql,engineId) =>
      resultMap.clear()
      resJson.clear()
      resJson.put("startTime", new Timestamp(System.currentTimeMillis))
      resJson.put("iql",iql)
      resJson.put("code",code)
      if(!code.trim.equals("")) interpreter.execute(code.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "")) //过滤掉注释
      //为当前iql设置groupId
      val groupId = BatchSQLRunnerEngine.getGroupId
      resJson.put("engineIdAndGroupId",engineId + ":" + groupId)
      sparkSession.sparkContext.clearJobGroup()
      sparkSession.sparkContext.setJobDescription("iql:" + iql)
      sparkSession.sparkContext.setJobGroup("iqlid:" + groupId,"iql:" + iql)
      //将该iql任务的唯一标识返回
      sender() ! resJson.toJSONString
      //解析iql并执行
      parse(iql.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", ""),
        new IQLSQLExecListener(sparkSession, null, resultMap))

    case GetResult(engineIdAndGroupId) =>
      if(jobMap.keySet().contains(engineIdAndGroupId)) {
        sender() ! jobMap.get(engineIdAndGroupId)
        jobMap.remove(engineIdAndGroupId)
      }else {
        sender() ! "{'status':'RUNNING'}"
      }

    case CancelJob(groupId) =>
      sparkSession.sparkContext.cancelJobGroup("iqlid:" + groupId)

    case StopIQL() => context.system.terminate()

    case _ => None
  }

  def parse(input: String, listener: IQLListener) = {
    resJson.put("status","FINISH")
    try {
      val loadLexer = new IQLLexer(new ANTLRInputStream(input))
      val tokens = new CommonTokenStream(loadLexer)
      val parser = new IQLParser(tokens)
      val stat = parser.statement()
      ParseTreeWalker.DEFAULT.walk(listener, stat)
      val endTime = System.currentTimeMillis()
      val take = (endTime - resJson.getTimestamp("startTime").getTime) / 1000
      resJson.put("hdfsPath",resultMap.getOrDefault("hdfsPath",""))
      resJson.put("schema",resultMap.getOrDefault("schema",""))
      resJson.put("takeTime",take)
      resJson.put("isSuccess",true)
    } catch {
      case e:Exception =>{
        e.printStackTrace()
        resJson.put("isSuccess",false)
        val out = new ByteArrayOutputStream()
        e.printStackTrace(new PrintStream(out))
        resJson.put("errorMessage", new String(out.toByteArray))
        out.close()
      }
    }
    jobMap.put(resJson.getString("engineIdAndGroupId"),resJson.toJSONString)
  }
}

object ExeActor{

  def props(spark: SparkSession): Props = Props(new ExeActor(spark))
//  def apply(spark: SparkSession): ExeActor = new ExeActor(spark)

}
