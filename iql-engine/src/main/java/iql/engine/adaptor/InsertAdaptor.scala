package iql.engine.adaptor

import iql.engine.IQLSQLExecListener
import iql.engine.antlr.IQLLexer
import iql.engine.antlr.IQLParser.SqlContext
import org.antlr.v4.runtime.misc.Interval

class InsertAdaptor(scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor {
  override def parse(ctx: SqlContext): Unit = {
    val input = ctx.start.getTokenSource.asInstanceOf[IQLLexer]._input
    val start = ctx.start.getStartIndex
    val stop = ctx.stop.getStopIndex
    val interval = new Interval(start, stop)
    val originalText = input.getText(interval)
    scriptSQLExecListener.sparkSession.sql(originalText).count()
  }
}
