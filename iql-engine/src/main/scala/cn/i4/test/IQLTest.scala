package cn.i4.test

import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import org.apache.spark.sql.{ForeachWriter, Row, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.streaming.Trigger

object IQLTest {

  case class GoodLuck(time: Int)

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
      .appName("iql")
      .master("local[4]")
      .enableHiveSupport()
      .getOrCreate()

    import spark.implicits._

//        spark.sparkContext.textFile("/tmp/recover_activation/nohup.out")
//          .filter(_.contains("连续失败次数")).map(r => r.substring(r.length - 1))
//          .map(r => GoodLuck(r.toInt)).toDF("time")
//          .groupBy("time")
//          .agg(count("time") as "count")
//          .sort("time")
//          .show(false)


    // Subscribe to 1 topic
    val df = spark
      .readStream
      .option("kafka.bootstrap.servers", "dsj02:9092,dsj03:9092,dsj04:9092,dsj04:9092")
      .option("subscribe", "i4-monitor")
      .option("startingoffsets", "latest")
      .option("failOnDataLoss", "false")
      .format("kafka")
      .load()
    spark.sparkContext.setLogLevel("WARN")

    val wordCounts = df.selectExpr("CAST(value AS STRING)")
      .as[String].groupBy("value").count()

    val query = wordCounts.writeStream.foreach(new ForeachWriter[Row] {
      override def process(value: Row): Unit = println(value.get(0))

      override def close(errorOrNull: Throwable): Unit = {}

      override def open(partitionId: Long, version: Long): Boolean = true
    })
        .outputMode("complete")
      .option("checkpointLocation", "/tmp/cp/cp81")
//      .format("console")
      .trigger(Trigger.ProcessingTime(5, TimeUnit.SECONDS))
      .start()
    query.awaitTermination()


  }
}

