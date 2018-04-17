package cn.i4.iql

import java.io.{ByteArrayOutputStream, PrintStream}
import java.util.concurrent.ConcurrentHashMap

import akka.actor.{Actor, ActorLogging, PoisonPill, Props}
import cn.i4.iql.antlr.{IQLLexer, IQLListener, IQLParser}
import cn.i4.iql.domain.Bean._
import cn.i4.iql.utils.BatchSQLRunnerEngine
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.antlr.v4.runtime.{ANTLRInputStream, CommonTokenStream}
import org.apache.spark.sql.SparkSession
import akka.actor.Props
import cn.i4.iql.repl.SparkInterpreter
import com.alibaba.fastjson.{JSONArray, JSONObject}


class ExeActor(spark: SparkSession) extends Actor with ActorLogging {

  var sparkSession: SparkSession = _
  var interpreter: SparkInterpreter = _
  var resultMap = new ConcurrentHashMap[String, String]()

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
    case Iql(code,iql) =>
      resultMap.clear()
      if(!code.trim.equals("")) interpreter.execute(code.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", "")) //过滤掉注释
      sparkSession.sparkContext.clearJobGroup()
      sparkSession.sparkContext.setJobDescription("iql:" + iql)
      sparkSession.sparkContext.setJobGroup("iqlid:" + BatchSQLRunnerEngine.getGroupId,"iql:" + iql)
      parse(iql.replaceAll("\\/\\/[^\\n]*|\\/\\*([^\\*^\\/]*|[\\*^\\/*]*|[^\\**\\/]*)*\\*+\\/", ""),
        new IQLSQLExecListener(sparkSession, null, resultMap))
    case StopIQL() => context.system.terminate()
    case s => println(s)
  }

  def parse(input: String, listener: IQLListener) = {
    val res = new JSONObject()
    try {
      val startTime = System.currentTimeMillis()
      val loadLexer = new IQLLexer(new ANTLRInputStream(input))
      val tokens = new CommonTokenStream(loadLexer)
      val parser = new IQLParser(tokens)
      val stat = parser.statement()
      ParseTreeWalker.DEFAULT.walk(listener, stat)
      val endTime = System.currentTimeMillis()
      val take = (endTime - startTime) / 1000
      res.put("hdfsPath",resultMap.getOrDefault("hdfsPath",""))
      res.put("schema",resultMap.getOrDefault("schema",""))
      res.put("takeTime",take)
      res.put("isSuccess",true)
    } catch {
      case e:Exception =>{
        e.printStackTrace()
        res.put("isSuccess",false)
        val out = new ByteArrayOutputStream()
        e.printStackTrace(new PrintStream(out))
        res.put("errorMessage", new String(out.toByteArray))
        out.close()
      }
    }
    sender() ! res.toJSONString
  }
}

object ExeActor{

  def props(spark: SparkSession): Props = Props(new ExeActor(spark))
//  def apply(spark: SparkSession): ExeActor = new ExeActor(spark)

}
