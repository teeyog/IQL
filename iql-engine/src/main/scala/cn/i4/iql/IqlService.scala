package cn.i4.iql

import java.util.concurrent.ConcurrentHashMap

import akka.actor.ActorSystem
import cn.i4.iql.utils.{AkkaUtils, PropsUtils}
import org.apache.spark.sql.SparkSession

object IqlService {

  var schedulerMode:Boolean = true
  val numActor:Int = 3

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
      .config("hive.exec.max.dynamic.partitions",20000)
      //序列化
      .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      //调度模式
      .config("spark.scheduler.mode", "FAIR")
//      .config("spark.scheduler.allocation.file","/home/runtime_file/fairscheduler.xml")
      .config("spark.yarn.executor.memoryOverhead","1024")
     .master("local[4]")
      .enableHiveSupport()
      .getOrCreate()


    val actorConf = AkkaUtils.getConfig
    val iqlSession = new IQLSession(actorConf.getString("akka.remote.netty.tcp.hostname") + ":" + actorConf.getString("akka.remote.netty.tcp.port"))
    val actorSystem = ActorSystem("iqlSystem", actorConf)
    (1 to numActor).foreach(id => actorSystem.actorOf(ExeActor.props(spark,iqlSession), name = "actor"+ id))
  }
}
