package cn.i4.iql

import akka.actor.ActorSystem
import cn.i4.iql.repl.SparkInterpreter
import cn.i4.iql.utils.{AkkaUtils, PropsUtils, ZkUtils}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object IqlService extends Logging {

    var schedulerMode: Boolean = true
    val numActor: Int = 3

    def createSpark(sparkConf: SparkConf) = {
        val spark = SparkSession
            .builder
            .appName("IQL")
            .config(sparkConf)
            //动态资源调整
            .config("spark.dynamicAllocation.enabled", "true")
            .config("spark.dynamicAllocation.executorIdleTimeout", "30s")
            .config("spark.dynamicAllocation.maxExecutors", "100")
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
            .config("spark.executor.memoryOverhead", "512")
//                                    .master("local[4]")
            .enableHiveSupport()
            .getOrCreate()
        spark.sparkContext.setLogLevel("WARN")
        spark
    }

    def main(args: Array[String]): Unit = {

        val interpreter = new SparkInterpreter()
        interpreter.start()
        val actorConf = AkkaUtils.getConfig(ZkUtils.getZkClient(PropsUtils.get("zkServers")))
        val iqlSession = new IQLSession(actorConf.getString("akka.remote.netty.tcp.hostname") + ":" + actorConf.getString("akka.remote.netty.tcp.port"))
        val actorSystem = ActorSystem("iqlSystem", actorConf)
        (1 to numActor).foreach(id => actorSystem.actorOf(ExeActor.props(interpreter, iqlSession), name = s"actor${id}"))
    }
}
