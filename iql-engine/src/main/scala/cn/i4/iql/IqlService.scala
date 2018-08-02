package cn.i4.iql

import akka.actor.ActorSystem
import cn.i4.iql.repl.Interpreter.{ExecuteAborted, ExecuteError, ExecuteIncomplete, ExecuteSuccess}
import cn.i4.iql.repl.SparkInterpreter
import cn.i4.iql.utils.AkkaUtils
import org.apache.spark.sql.SparkSession

object IqlService {

  var schedulerMode:Boolean = true
  val numActor:Int = 2

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .appName("IQL")
      //动态资源调整
      .config("spark.dynamicAllocation.enabled", "true")
      .config("spark.dynamicAllocation.executorIdleTimeout", "30s")
      .config("spark.dynamicAllocation.maxExecutors", "60")
      .config("spark.dynamicAllocation.minExecutors", "0")
      //动态分区
      .config("hive.exec.dynamic.partition", "true")
      .config("hive.exec.dynamic.partition.mode", "nonstrict")
      .config("hive.exec.max.dynamic.partitions",20000)
      //序列化
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      //调度模式
      .config("spark.scheduler.mode", "FAIR")
//      .config("spark.scheduler.allocation.file","/home/runtime_file/fairscheduler.xml")
      .config("spark.executor.memoryOverhead","1024")
     .master("local[4]")
      .enableHiveSupport()
      .getOrCreate()

//    val interpreter = new SparkInterpreter(spark)
//    interpreter.start()
//
//    val withoutUdfString:String = Array(
//      "import spark.implicits._",
//      "val df = spark.sparkContext.parallelize(Seq('interpreted without udf', 'foo','bar')).toDF",
//      "df.show(false)",
//      "df.createOrReplaceTempView('df')"
//    ).mkString("\n").replaceAll("'", "\"")
//
//    println(withoutUdfString)
//
//    interpreter.execute(withoutUdfString) match {
//      case _: ExecuteIncomplete =>
//      case e: ExecuteSuccess =>
//        println(e.content)
//      case e: ExecuteError =>
//        println(e.ename + "--" + e.evalue)
//      case e: ExecuteAborted =>
//        println(e.message)
//      case _ =>
//    }


    val actorConf = AkkaUtils.getConfig
    val iqlSession = new IQLSession(actorConf.getString("akka.remote.netty.tcp.hostname") + ":" + actorConf.getString("akka.remote.netty.tcp.port"))
    val actorSystem = ActorSystem("iqlSystem", actorConf)
    (1 to numActor).foreach(id => actorSystem.actorOf(ExeActor.props(spark,iqlSession), name = "actor"+ id))
  }
}
