package iql.engine.test

import java.util.Random

object IQLTest {

  case class GoodLuck(time: Int)



  def main(args: Array[String]): Unit = {

    val array = Array("大","小")

//    val spark = SparkSession
//      .builder
//      .appName("iql")
//      .master("local[4]")
//      .enableHiveSupport()
//      .getOrCreate()
//
//    import spark.implicits._
//
//    spark.sparkContext.textFile("/tmp/recover_activation/nohup.out")
//      .filter(r => r.contains("第") && r.contains("期"))
//      .map(r => (r.split("：")(1).substring(0, 1),array((new util.Random).nextInt(2))))
//        .map(r => if(r._1 != r._2) "---" else "")
//      .foreach(println)


    val random = new Random()
    (0 to 20).foreach(r => println(random.nextInt(2)))


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

