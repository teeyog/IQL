package org.apache.spark.sql.jdbc

import cn.i4.iql.utils.PropsUtils
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.apache.spark.sql.execution.streaming.Sink

class JdbcSink (
                 sqlContext: SQLContext,
                 parameters: Map[String, String]) extends Sink with Logging {
  @volatile private var latestBatchId = -1L

  override def toString(): String = "JdbcSink"

  override def addBatch(batchId: Long, data: DataFrame): Unit = {
    if (batchId <= latestBatchId) {
      logInfo(s"Skipping already committed batch $batchId")
    } else {
      data.write
        .format("jdbc")
        .mode(SaveMode.Append)
        .options(parameters)
        .save()
    }
  }
}
