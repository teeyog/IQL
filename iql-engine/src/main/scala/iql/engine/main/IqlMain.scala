package iql.engine.main

import akka.actor.ActorSystem
import iql.common.Logging
import iql.common.utils.{AkkaUtils, ZkUtils}
import iql.engine.config.IQL_PARALLELISM
import iql.engine.{ExeActor, IQLSession}
import iql.engine.repl.SparkInterpreter
import iql.engine.utils.PropsUtils
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

object IqlMain extends Logging {

    var schedulerMode: Boolean = true

    def parseArgs(args: Array[String]) = {
        var argsMap:Map[String,String] = Map()
        var argv = args.toList
        while (argv.nonEmpty) {
            argv match {
                case "-iql.zkServers" :: value :: tail =>
                    argsMap += ("zkServers" -> value)
                    argv = tail
                case Nil =>
                case tail =>
                    // scalastyle:off println
                    System.err.println(s"Unrecognized options: ${tail.mkString(" ")}")
            }
        }
        argsMap
    }

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
            //调度模式
            .config("spark.scheduler.mode", "FAIR")
            .config("spark.executor.memoryOverhead", "512")
//          .master("local[*]")
            .enableHiveSupport()
            .getOrCreate()
        spark.sparkContext.setLogLevel("WARN")
        spark
    }

    def main(args: Array[String]): Unit = {
        val argsMap = parseArgs(args)
        val interpreter = new SparkInterpreter()
        val sparkConf = interpreter.start()
        val actorConf = AkkaUtils.getConfig(ZkUtils.getZkClient(argsMap.getOrElse("zkServers",PropsUtils.get("zkServers"))))
        val iqlSession = new IQLSession(actorConf.getString("akka.remote.netty.tcp.hostname") + ":" + actorConf.getString("akka.remote.netty.tcp.port"))
        val actorSystem = ActorSystem("iqlSystem", actorConf)
        (1 to sparkConf.getInt(IQL_PARALLELISM.key, IQL_PARALLELISM.defaultValue.get))
            .foreach(id => actorSystem.actorOf(ExeActor.props(interpreter, iqlSession, sparkConf), name = s"actor$id"))
        iqlSession.awaitTermination()
    }

}
