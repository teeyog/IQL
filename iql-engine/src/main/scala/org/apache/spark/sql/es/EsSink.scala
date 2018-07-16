package org.apache.spark.sql.es

import org.apache.spark.internal.Logging
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.sql.execution.streaming.Sink
import org.apache.spark.sql.functions._
import org.elasticsearch.spark._

class EsSink(
              sqlContext: SQLContext,
              parameters: Map[String, String],
              resource: Option[String]) extends Sink with Logging {
  @volatile private var latestBatchId = -1L

  override def toString(): String = "EsSink"

  override def addBatch(batchId: Long, data: DataFrame): Unit = {
    import sqlContext.sparkSession.implicits._
    if (batchId <= latestBatchId) {
      logInfo(s"Skipping already committed batch $batchId")
    } else {
      require(resource.nonEmpty, "resource must no be empty!")
      data.select(to_json(struct("*")) as "jsonStr")
        .map(_.getString(0)).rdd
        .saveJsonToEs(resource.get, parameters)
    }
  }
}
