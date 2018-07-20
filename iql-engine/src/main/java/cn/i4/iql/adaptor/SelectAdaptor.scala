package cn.i4.iql.adaptor

import cn.i4.iql.IQLSQLExecListener
import cn.i4.iql.antlr.IQLLexer
import cn.i4.iql.antlr.IQLParser.SqlContext
import org.antlr.v4.runtime.misc.Interval

class SelectAdaptor(scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor {
  override def parse(ctx: SqlContext): Unit = {
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
      val df_result = scriptSQLExecListener.sparkSession.sql(sql)
      scriptSQLExecListener.addResult("schema", df_result.schema.fields.map(_.name).mkString(","))
      df_result.write.option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ").json(hdfsPath)
      scriptSQLExecListener.addResult("hdfsPath", hdfsPath)
    } else {
      scriptSQLExecListener.sparkSession.sql(sql).createOrReplaceTempView(tableName)
    }

  }
}
