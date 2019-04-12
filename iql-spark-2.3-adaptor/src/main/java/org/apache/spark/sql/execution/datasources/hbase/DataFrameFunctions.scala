package org.apache.spark.sql.execution.datasources.hbase

import org.apache.hadoop.fs.Path
import org.apache.hadoop.hbase.{HBaseConfiguration, KeyValue, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, HTable, Put, Result}
import org.apache.hadoop.hbase.io.ImmutableBytesWritable
import org.apache.hadoop.hbase.mapreduce.{HFileOutputFormat2, LoadIncrementalHFiles, TableOutputFormat}
import org.apache.hadoop.mapred.JobConf
import org.apache.hadoop.mapreduce.Job
import org.apache.spark.internal.Logging
import org.apache.spark.sql.{DataFrame, Row}
import org.apache.spark.util.SerializableConfiguration
import org.json4s.DefaultFormats
import scala.collection.immutable.HashMap

class DataFrameFunctions(data: DataFrame) extends Logging with Serializable {

  def saveToHbase(tableName: String, zkUrl: Option[String] = None,
                  options: Map[String, String] = new HashMap[String, String]): Unit = {

    val wrappedConf = {
      implicit val formats = DefaultFormats
      val hc = HBaseConfiguration.create()
      hc.set("hbase.zookeeper.quorum", zkUrl.getOrElse("localhost:2181"))
      hc.set("hbase.client.keyvalue.maxsize", "104857600")
      options.foreach(p => hc.set(p._1, p._2))
      data.sparkSession.sparkContext.getConf.getAll
        .filter(_._1.toLowerCase.contains("hbase"))
        .foreach(p => hc.set(p._1, p._2))
      new SerializableConfiguration(hc)
    }

    val hbaseConf = wrappedConf.value

    val rowkey = options.getOrElse("hbase.table.rowkey.field", data.schema.head.name)
    require(data.schema.fields.map(_.name).contains(rowkey), "No field name named " + rowkey)
    log.warn("The rowkey field name is " + rowkey)
    if (!options.contains("hbase.table.rowkey.field")) log.warn("The hbase.table.rowkey.field is not set, and the first field is rowkey as the default `" + data.schema.head.name + "`")
    val family = options.getOrElse("hbase.table.family", "info")
    if (!options.contains("hbase.table.family")) log.warn("The hbase.table.family is not set, use the default family is `info`")
    val numReg = options.getOrElse("hbase.table.numReg", -1).toString.toInt
    val rowkeyPrefix = options.getOrElse("hbase.table.rowkey.prefix", null)
    val regionSplits = options.getOrElse("hbase.table.region.splits", null)
    val startKey = options.getOrElse("hbase.table.startKey", options.getOrElse("hbase.table.startkey", null))
    val endKey = options.getOrElse("hbase.table.endKey", options.getOrElse("hbase.table.endkey", null))

    val rdd = data.rdd //df.queryExecution.toRdd
    val f = family

    if (options.getOrElse("hbase.check_table", "false").toBoolean) {
      val tName = TableName.valueOf(tableName)
      val connection = ConnectionFactory.createConnection(hbaseConf)
      val admin = connection.getAdmin
      if (!admin.isTableAvailable(tName)) {
        HBaseUtils.createTable(connection, tName, family, startKey, endKey, numReg, rowkeyPrefix, regionSplits)
      }
      connection.close()
    }
    if (hbaseConf.get("mapreduce.output.fileoutputformat.outputdir") == null) {
      hbaseConf.set("mapreduce.output.fileoutputformat.outputdir", "/tmp")
    }
    val jobConf = new JobConf(hbaseConf, this.getClass)
    jobConf.set(TableOutputFormat.OUTPUT_TABLE, tableName)

    val job = Job.getInstance(jobConf)
    job.setOutputKeyClass(classOf[ImmutableBytesWritable])
    job.setOutputValueClass(classOf[Result])
    job.setOutputFormatClass(classOf[TableOutputFormat[ImmutableBytesWritable]])

    val fields = data.schema.toArray
    val rowkeyField = fields.zipWithIndex.filter(f => f._1.name == rowkey).head
    val otherFields = fields.zipWithIndex.filter(f => f._1.name != rowkey)

    lazy val rowkeySetter = HBaseUtils.makeRowkeySetter(rowkeyField)
    lazy val setters = otherFields.map(r => HBaseUtils.makeHbaseSetter(r))
    lazy val setters_bulkload =
      otherFields.sortBy(_._1.name).map(r => HBaseUtils.makeHbaseSetter_bulkload(r))

    options.getOrElse("bulkload.enable", "false") match {

      case "true" =>
        val tmpPath = s"/tmp/bulkload/${tableName}" + System.currentTimeMillis()

        def convertToPut_bulkload(row: Row) = {
          val rk = rowkeySetter.apply(row)
          setters_bulkload.map(_.apply(rk, row, f))
        }

        rdd.flatMap(convertToPut_bulkload)
          .saveAsNewAPIHadoopFile(tmpPath, classOf[ImmutableBytesWritable], classOf[KeyValue], classOf[HFileOutputFormat2], job.getConfiguration)

        val bulkLoader: LoadIncrementalHFiles = new LoadIncrementalHFiles(hbaseConf)
        bulkLoader.doBulkLoad(new Path(tmpPath), new HTable(hbaseConf, tableName))

      case "false" =>
        def convertToPut(row: Row) = {
          val put = new Put(rowkeySetter.apply(row))
          setters.foreach(_.apply(put, row, f))
          (new ImmutableBytesWritable, put)
        }

        rdd.map(convertToPut).saveAsNewAPIHadoopDataset(job.getConfiguration)
    }
  }
}