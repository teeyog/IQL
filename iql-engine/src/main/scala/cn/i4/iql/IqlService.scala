package cn.i4.iql

import akka.actor.{ActorSystem, Props}
import cn.i4.iql.repl.Interpreter.{ExecuteAborted, ExecuteError, ExecuteIncomplete, ExecuteSuccess}
import cn.i4.iql.repl.SparkInterpreter
import cn.i4.iql.utils.AkkaUtils
import org.apache.spark.sql.SparkSession

object IqlService extends Logging {

    var schedulerMode: Boolean = true
    val numActor: Int = 3


    def createSpark() = {
        SparkSession
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
            .config("hive.exec.max.dynamic.partitions", 20000)
            //序列化
            .config("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
            //调度模式
            .config("spark.scheduler.mode", "FAIR")
            .config("spark.scheduler.allocation.file", "/home/runtime_file/fairscheduler.xml")
            .config("spark.executor.memoryOverhead", "1024")
//                        .master("local[4]")
            .enableHiveSupport()
            .getOrCreate()
    }

    def main(args: Array[String]): Unit = {

        val actorConf = AkkaUtils.getConfig
        val iqlSession = new IQLSession(actorConf.getString("akka.remote.netty.tcp.hostname") + ":" + actorConf.getString("akka.remote.netty.tcp.port"))
        val actorSystem = ActorSystem("iqlSystem", actorConf)
        (1 to numActor).foreach(id => actorSystem.actorOf(ExeActor.props(iqlSession), name = s"actor${id}"))

//                val system = ActorSystem("HelloSystem")
//                // create and start the actor
//                val helloActor = system.actorOf(Props[TestActor], name = "helloActor")
//                // send two messages
//                helloActor ! """spark.sparkContext.parallelize(Seq(("A",12),("B",13))).reduceByKey(_+_).foreach(println)"""
//                Thread.sleep(40000)
//                IqlService.createSpark()

    }
}
