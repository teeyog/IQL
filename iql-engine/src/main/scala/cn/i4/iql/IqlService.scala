package cn.i4.iql

import java.util.concurrent.ConcurrentHashMap

import akka.actor.ActorSystem
import cn.i4.iql.utils.AkkaUtils
import org.apache.spark.sql.SparkSession

object IqlService {

  var engineInfo:String = null
  val jobMap = new ConcurrentHashMap[String, String]()
  var schedulerMode:Boolean = true

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .appName("IQL")
      //动态资源调整
      .config("spark.dynamicAllocation.enabled", "true")
      .config("spark.dynamicAllocation.executorIdleTimeout", "30s")
      .config("spark.dynamicAllocation.maxExecutors", "60")
      //动态分区
      .config("hive.exec.dynamic.partition", "true")
      .config("hive.exec.dynamic.partition.mode", "nonstrict")
      //序列化
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      //调度模式
      .config("spark.scheduler.mode", "FAIR")
//      .config("spark.scheduler.allocation.file","/home/runtime_file/fairscheduler.xml")
      .config("spark.yarn.executor.memoryOverhead","1024")
            .master("local[4]")
      .enableHiveSupport()
      .getOrCreate()


    spark.sparkContext.setLogLevel("WARN")

    val actorConf = AkkaUtils.getConfig
    engineInfo = actorConf.getString("akka.remote.netty.tcp.hostname") + ":" + actorConf.getString("akka.remote.netty.tcp.port")

    val numActor:Int = 3
    val actorSystem = ActorSystem("iqlSystem", actorConf)
    for(id <- 1 to numActor){
      actorSystem.actorOf(ExeActor.props(spark), name = "actor"+id)
    }
  }
}
