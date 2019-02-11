package iql.engine.adaptor

import java.util.UUID

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

    val uuidTable = UUID.randomUUID().toString.replace("-","")
    scriptSQLExecListener.addResult("uuidTable", uuidTable)
    scriptSQLExecListener.sparkSession.sql(sql).createOrReplaceTempView(uuidTable)
  }
}
