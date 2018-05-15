package cn.i4.iql.adaptor

import cn.i4.iql.IQLSQLExecListener
import cn.i4.iql.antlr.IQLParser._
import cn.i4.iql.utils.{ProTools}
import org.apache.spark.sql._

class LoadAdaptor(scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor {
  override def parse(ctx: SqlContext): Unit = {
    val reader = scriptSQLExecListener.sparkSession.read
    var table: DataFrame = null
    var format = ""
    var option = Map[String, String]()
    var path = ""
    var tableName = ""
    (0 to ctx.getChildCount() - 1).foreach { tokenIndex =>
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
    reader.options(option)
    format match {
      case "jdbc" =>
        reader
          .option("dbtable", path)
          .option("driver",option.getOrElse("driver",ProTools.get("jdbc.driver")))
          .option("url",option.getOrElse("url",ProTools.get("jdbc.url")))
        table = reader.format("jdbc").load()

      case "es" | "org.elasticsearch.spark.sql" =>
        table = reader.format("org.elasticsearch.spark.sql").load(path)

      case "hbase" | "org.apache.spark.sql.execution.datasources.hbase" =>
        reader.option("hbase.table.name",path)
        table = reader.format("org.apache.spark.sql.execution.datasources.hbase").load()

      case "kafka" | "org.apache.spark.sql.execution.datasources.kafka" =>
        reader.option("metadata.broker.list",option.getOrElse("metadata.broker.list", ProTools.get("kafka.metadata.broker.list")))
        reader.option("zookeeper.connect",option.getOrElse("zookeeper.connect",ProTools.get("kafka.zookeeper.connect")))
        reader.option("topics",path)
        table = reader.format("org.apache.spark.sql.execution.datasources.kafka").load()
        if(option.getOrElse("data.type","json").toLowerCase.equals("json"))
          table = scriptSQLExecListener.sparkSession.read.json(table.select("msg").rdd.map(_.getString(0)))

      case "json" | "csv" | "orc" | "parquet" | "text" =>
        if(path.startsWith("'") || path.startsWith("`") || path.startsWith("\"")) path = path.substring(1)
        if(path.endsWith("'") || path.endsWith("`") || path.endsWith("\"")) path = path.substring(0,path.length - 1)
        table = reader.option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ").option("header", "true").format(format).load(path)

      case _ =>
        table = reader.format(format).load(withPathPrefix(scriptSQLExecListener.pathPrefix, cleanStr(path)))
    }
    table.createOrReplaceTempView(tableName)
  }
}
