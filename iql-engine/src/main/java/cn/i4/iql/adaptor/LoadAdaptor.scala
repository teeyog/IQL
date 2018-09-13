package cn.i4.iql.adaptor

import cn.i4.iql.IQLSQLExecListener
import cn.i4.iql.antlr.IQLParser._
import cn.i4.iql.utils.PropsUtils
import org.apache.spark.sql._

class LoadAdaptor(scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor with DslTool{
  override def parse(ctx: SqlContext): Unit = {
    var format = ""
    var option = Map[String, String]()
    var path = ""
    var tableName = ""
    (0 until ctx.getChildCount).foreach { tokenIndex =>
      ctx.getChild(tokenIndex) match {
        case s: FormatContext =>
          format = s.getText
        case s: ExpressionContext =>
          option += (cleanStr(s.identifier().getText) -> cleanStr(s.STRING().getText))
        case s: BooleanExpressionContext =>
          option += (cleanStr(s.expression().identifier().getText) -> cleanStr(s.expression().STRING().getText))
        case s: PathContext =>
          path = cleanStr(s.getText)
        case s: TableNameContext =>
          tableName = s.getText
        case _ =>
      }
    }
    if (option.contains("spark.job.mode") && option("spark.job.mode").equalsIgnoreCase("stream")) {
      scriptSQLExecListener.addEnv("stream", "true")
      new StreamLoadAdaptor(scriptSQLExecListener, option, path, tableName, format).parse
    } else {
      new BatchLoadAdaptor(scriptSQLExecListener, option, path, tableName, format).parse
    }
  }
}

class BatchLoadAdaptor(scriptSQLExecListener: IQLSQLExecListener,
                       option: Map[String, String],
                       var path: String,
                       tableName: String,
                       format: String
                      )  extends DslTool {
  def parse = {
    var table: DataFrame = null
    val sparkSession = scriptSQLExecListener.sparkSession
    val reader = scriptSQLExecListener.sparkSession.read
    reader.options(option).option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ")
    format match {
      case "jdbc" =>
        reader
          .option("dbtable", path)
          .option("driver", option.getOrElse("driver", PropsUtils.get("jdbc.driver")))
          .option("url", option.getOrElse("url", PropsUtils.get("jdbc.url")))
        table = reader.format("jdbc").load()

      case "es" | "org.elasticsearch.spark.sql" =>
        table = reader.format("org.elasticsearch.spark.sql").load(path)

      case "hbase" | "org.apache.spark.sql.execution.datasources.hbase" =>
        reader
          .option("hbase.table.name", path)
          .option("hbase.zookeeper.quorum", option.getOrElse("hbase.zookeeper.quorum", PropsUtils.get("hbase.zookeeper.quorum")))
        table = reader.format("org.apache.spark.sql.execution.datasources.hbase").load()

      case "kafka" | "org.apache.spark.sql.execution.datasources.kafka" =>
        reader.option("metadata.broker.list", option.getOrElse("metadata.broker.list", PropsUtils.get("kafka.metadata.broker.list")))
        reader.option("zookeeper.connect", option.getOrElse("zookeeper.connect", PropsUtils.get("kafka.zookeeper.connect")))
        reader.option("topics", path)
        table = reader.format("org.apache.spark.sql.execution.datasources.kafka").load()
        if (option.getOrElse("data.type", "json").toLowerCase.equals("json")){
//          val JSON_REGEX = option.getOrElse("json_regex","(.*)").r
          table = sparkSession.read.json(
            table.select("msg").rdd.map(_.getString(0))
//            table.select("msg").rdd.map(r => {
//              r.getString(0) match {
//                case JSON_REGEX(jsonStr) => jsonStr
//              }
//            })
          )
        }

      case "json" | "csv" | "orc" | "parquet" | "text" =>
        if (option.contains("schema")) {
          reader.schema(option("schema"))
        }
        table = reader.option("header", "true").format(format).load(path)

      case "jsonStr" =>
        import sparkSession.implicits._
        val items = cleanStr(path).split("\n")
        table = reader.json(sparkSession.createDataset[String](items))
      case _ =>
        table = reader.format(format).load(path)
    }
    table.createOrReplaceTempView(tableName)
  }
}

class StreamLoadAdaptor(scriptSQLExecListener: IQLSQLExecListener,
                        option: Map[String, String],
                        path: String,
                        tableName: String,
                        format: String
                       ) {

  def withWaterMark(table: DataFrame, option: Map[String, String]) = {
    if (option.contains("eventTimeCol")) {
      table.withWatermark(option("eventTimeCol"), option("delayThreshold"))
    } else {
      table
    }
  }
  def parse = {
    var table: DataFrame = null
    val reader = scriptSQLExecListener.sparkSession.readStream
    format match {
      case "kafka" =>
        reader.option("kafka.bootstrap.servers", option.getOrElse("kafka.bootstrap.servers", PropsUtils.get("kafka.metadata.broker.list")))
        reader.option("subscribe", path)
        table = reader.options(option).format(format).load()
            .selectExpr("CAST(key AS STRING)","CAST(value AS STRING)","topic","partition","offset","timestamp","timestampType")
      case _ =>
    }
    table = withWaterMark(table, option)
    table.createOrReplaceTempView(tableName)
  }
}