package iql.engine.adaptor

import iql.engine.IQLSQLExecListener
import iql.engine.antlr.IQLLexer
import iql.engine.antlr.IQLParser.SqlContext
import org.antlr.v4.runtime.misc.Interval

//show create table ods.ods_user_mbl
class ShowAdaptor(scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor {
  override def parse(ctx: SqlContext): Unit = {
    val input = ctx.start.getTokenSource.asInstanceOf[IQLLexer]._input
    val start = ctx.start.getStartIndex
    val stop = ctx.stop.getStopIndex
    val interval = new Interval(start, stop)
    val sql = input.getText(interval)

    val hdfsPath = "/tmp/iql/result/iql_query_result_" + System.currentTimeMillis()
    val df_result = scriptSQLExecListener.sparkSession.sql(sql)
    scriptSQLExecListener.addResult("schema", df_result.schema.fields.map(_.name).mkString(","))
    df_result.write.option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ").json(hdfsPath)
    scriptSQLExecListener.addResult("hdfsPath", hdfsPath)
  }
}
