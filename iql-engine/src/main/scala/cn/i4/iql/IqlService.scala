package cn.i4.iql

import akka.actor.ActorSystem
import cn.i4.iql.repl.Interpreter.ExecuteSuccess
import cn.i4.iql.repl.SparkInterpreter
import cn.i4.iql.utils.AkkaUtils
import org.apache.spark.sql.SparkSession

object IqlService {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .appName("IQL")
      .config("spark.dynamicAllocation.enabled", "true")
      .config("spark.dynamicAllocation.executorIdleTimeout", "30s")
      .config("spark.dynamicAllocation.maxExecutors", "100")
      .config("hive.exec.dynamic.partition", "true")
      .config("hive.exec.dynamic.partition.mode", "nonstrict")
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .config("spark.yarn.executor.memoryOverhead","2048")
//            .master("local[4]")
      .enableHiveSupport()
      .getOrCreate()

    spark.sparkContext.setLogLevel("WARN")

    val actorSystem = ActorSystem("iqlSystem", AkkaUtils.getConfig)
    actorSystem.actorOf(ExeActor.props(spark), name = "actor1")
    actorSystem.actorOf(ExeActor.props(spark), name = "actor2")
    actorSystem.actorOf(ExeActor.props(spark), name = "actor3")
  }
}
