package cn.i4.iql.adaptor

import cn.i4.iql.IQLSQLExecListener
import cn.i4.iql.antlr.IQLParser._
import cn.i4.iql.utils.PropsUtils
import org.apache.spark.sql._

//save new_tr as json.`/tmp/todd
class SaveAdaptor(scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor {
  override def parse(ctx: SqlContext): Unit = {
    var writer: DataFrameWriter[Row] = null
    var oldDF: DataFrame = null
    var mode = SaveMode.ErrorIfExists
    var final_path = ""
    var format = ""
    var option = Map[String, String]()
    var tableName = ""
    var partitionByCol = Array[String]()
    var numPartition:Int = 1

    (0 to ctx.getChildCount() - 1).foreach { tokenIndex =>
      ctx.getChild(tokenIndex) match {
        case s: FormatContext =>
          format = s.getText
          format match {
            case "hive" =>
            case _ =>
              format = s.getText
          }
        case s: PathContext =>
          format match {
            case "jdbc" | "hive" | "kafka8" | "kafka9" | "hbase" | "redis" | "es" | "json" =>
              final_path = cleanStr(s.getText)
            case _ =>
              final_path = withPathPrefix(scriptSQLExecListener.pathPrefix, cleanStr(s.getText))
          }
        case s: TableNameContext =>
          tableName = s.getText
          oldDF = scriptSQLExecListener.sparkSession.table(s.getText)
        case s: OverwriteContext =>
          mode = SaveMode.Overwrite
        case s: AppendContext =>
          mode = SaveMode.Append
        case s: ErrorIfExistsContext =>
          mode = SaveMode.ErrorIfExists
        case s: IgnoreContext =>
          mode = SaveMode.Ignore
        case s: ColContext =>
          partitionByCol = cleanStr(s.getText).split(",")
        case s: ExpressionContext =>
          option += (cleanStr(s.identifier().getText) -> cleanStr(s.STRING().getText))
        case s: BooleanExpressionContext =>
          option += (cleanStr(s.expression().identifier().getText) -> cleanStr(s.expression().STRING().getText))
        case s: NumPartitionContext =>
          numPartition = s.getText.toInt
        case _ =>
      }
    }

//    val dbAndTable = final_path.split("\\.")
//    var connect_provied = false
//    if (dbAndTable.length == 2 && dbMap.containsKey(dbAndTable(0))) {
//      dbMap.get(dbAndTable(0)).foreach {
//        f =>
//          writer.option(f._1, f._2)
//      }
//      connect_provied = true
//    }
//
//    if (connect_provied) {
//      final_path = dbAndTable(1)
//    }

    if (option.contains("fileNum")) {
      oldDF = oldDF.repartition(option.getOrElse("fileNum", "").toString.toInt)
    }
    writer = oldDF.write
    writer = writer.format(format).mode(mode).partitionBy(partitionByCol: _*).options(option)
    format match {
      case "json" | "csv" | "orc" | "parquet" | "text" =>
        val tmpPath = "/tmp/iql/tmp/"+System.currentTimeMillis()
        writer.option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ").option("header", "true").format(format).save(tmpPath)//写
        scriptSQLExecListener.sparkSession.read.format(format).load(tmpPath).coalesce(numPartition)//读
          .write.mode(mode).partitionBy(partitionByCol: _*).options(option).option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ").option("header", "true")
          .format(format).save(final_path)//写
      case "es" =>
        writer.save(final_path)
      case "hive" =>
        writer.format(option.getOrElse("file_format", "parquet"))
        writer.saveAsTable(final_path)
      case "kafka8" | "kafka9" =>
        writer.option("topics", final_path).format("com.hortonworks.spark.sql.kafka08").save()
      case "hbase" =>
        writer.option("hbase.table.name", final_path).format("org.apache.spark.sql.execution.datasources.hbase").save()
      case "redis" =>
        writer.option("outputTableName", final_path).format("org.apache.spark.sql.execution.datasources.redis").save()
      case "jdbc" =>
        writer
          .option("driver",option.getOrElse("driver",PropsUtils.props.getProperty("jdbc.driver")))
          .option("url",option.getOrElse("url",PropsUtils.props.getProperty("jdbc.url")))
          .option("dbtable",final_path)
          .save()
      case _ =>
        writer.save(final_path)
    }
  }
}
