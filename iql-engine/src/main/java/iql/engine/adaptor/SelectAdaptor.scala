package iql.engine.adaptor

import iql.engine.IQLSQLExecListener
import iql.engine.antlr.IQLLexer
import iql.engine.antlr.IQLParser.SqlContext
import org.antlr.v4.runtime.misc.Interval
import org.apache.spark.sql.functions._

class SelectAdaptor(scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor {
  override def parse(ctx: SqlContext): Unit = {
    val sparkSession = scriptSQLExecListener.sparkSession
    val input = ctx.start.getTokenSource.asInstanceOf[IQLLexer]._input
    val start = ctx.start.getStartIndex
    val stop = ctx.stop.getStopIndex
    val interval = new Interval(start, stop)
    val originalText = input.getText(interval)
    var tableName = ""

    val chunks = originalText.split("\\s+")
    if(chunks(chunks.length - 2).equals("as")) tableName = chunks.last.replace(";", "")
    val sql = originalText.replaceAll(s"as[\\s|\\n]+${tableName}", "")
    if(tableName.equals("")){
      val hdfsPath = "/tmp/iql/result/iql_query_result_" + System.currentTimeMillis()
      val df_result = sparkSession.sql(sql)
      scriptSQLExecListener.addResult("schema", df_result.schema.fields.map(_.name).mkString(","))
      df_result.select(df_result.columns.map(col(_).cast("String")):_*).write.option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ").json(hdfsPath)
      scriptSQLExecListener.addResult("hdfsPath", hdfsPath)
    } else {
      sparkSession.sql(sql).createOrReplaceTempView(tableName)
    }

  }
}
