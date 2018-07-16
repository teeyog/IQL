package org.apache.spark.sql.jdbc

import cn.i4.iql.utils.PropsUtils
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{DataFrame, SQLContext, SaveMode}
import org.apache.spark.sql.execution.streaming.Sink

class JdbcSink (
                 sqlContext: SQLContext,
                 parameters: Map[String, String],
                 dbtable: Option[String]) extends Sink with Logging {
  @volatile private var latestBatchId = -1L

  override def toString(): String = "JdbcSink"

  override def addBatch(batchId: Long, data: DataFrame): Unit = {
    if (batchId <= latestBatchId) {
      logInfo(s"Skipping already committed batch $batchId")
    } else {
      require(dbtable.nonEmpty,"dbtable must no be empty!")
      data.write
        .format("jdbc")
        .mode(SaveMode.Append)
        .options(parameters)
        .option("driver",parameters.getOrElse("driver",PropsUtils.get("jdbc.driver")))
        .option("url",parameters.getOrElse("url",PropsUtils.get("jdbc.url")))
        .option("dbtable",dbtable.get)
        .save()
    }
  }
}
