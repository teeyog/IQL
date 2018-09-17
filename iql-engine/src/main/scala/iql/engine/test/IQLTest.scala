package iql.engine.test

import iql.engine.auth.CheckAuth
import org.apache.spark.sql.SparkSession

object IQLTest {

  def main(args: Array[String]): Unit = {


    val spark = SparkSession
        .builder
        .appName("IQL")
        .master("local[4]")
        .enableHiveSupport()
        .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")


    val df = spark.sql(
      """
select idfa,uuid from mc.mbl where date='20180912' limit 10
        """.stripMargin)
    println(df.queryExecution.analyzed)


    // Subscribe to 1 topic
    //    val df = spark
    //      .readStream
    //      .option("kafka.bootstrap.servers", "dsj02:9092,dsj03:9092,dsj04:9092,dsj04:9092")
    //      .option("subscribe", "i4-monitor")
    //      .option("startingoffsets", "latest")
    //      .option("failOnDataLoss", "false")
    //      .format("kafka")
    //      .load()
    //    spark.sparkContext.setLogLevel("WARN")
    //
    //    val wordCounts = df.selectExpr("CAST(value AS STRING)")
    //      .as[String].groupBy("value").count()
    //
    //    val query = wordCounts.writeStream.foreach(new ForeachWriter[Row] {
    //      override def process(value: Row): Unit = println(value.get(0))
    //
    //      override def close(errorOrNull: Throwable): Unit = {}
    //
    //      override def open(partitionId: Long, version: Long): Boolean = true
    //    })
    //      .outputMode("complete")
    //      .option("checkpointLocation", "/tmp/cp/cp81")
    //      .format("console")
    //      .trigger(Trigger.ProcessingTime(5, TimeUnit.SECONDS))
    //      .start()
    //    query.awaitTermination()


  }
}

