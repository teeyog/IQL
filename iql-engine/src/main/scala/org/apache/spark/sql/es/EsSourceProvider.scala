package org.apache.spark.sql.es

import org.apache.spark.internal.Logging
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.execution.streaming.Sink
import org.apache.spark.sql.sources._
import org.apache.spark.sql.streaming.OutputMode

class EsSourceProvider extends DataSourceRegister
  with StreamSinkProvider
  with Logging {

  override def shortName(): String = "es"

  override def createSink(sqlContext: SQLContext,
                          parameters: Map[String, String],
                          partitionColumns: Seq[String],
                          outputMode: OutputMode): Sink = {
    new EsSink(sqlContext, parameters, parameters.get("resource").map(_.trim))
  }
}