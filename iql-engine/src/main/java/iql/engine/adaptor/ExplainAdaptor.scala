package iql.engine.adaptor

import java.util.UUID

import iql.engine.IQLSQLExecListener
import iql.engine.antlr.IQLLexer
import iql.engine.antlr.IQLParser.SqlContext
import org.antlr.v4.runtime.misc.Interval

class ExplainAdaptor(scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor {
    override def parse(ctx: SqlContext): Unit = {
        val sparkSession = scriptSQLExecListener.sparkSession
        val input = ctx.start.getTokenSource.asInstanceOf[IQLLexer]._input
        val start = ctx.start.getStartIndex
        val stop = ctx.stop.getStopIndex
        val interval = new Interval(start, stop)
        val originalText = input.getText(interval)

        val chunks = originalText.replace(";", "").split("\\s+")
        chunks.length match {
            case 2 =>
                scriptSQLExecListener.addResult("explainStr", sparkSession.table(chunks(1)).queryExecution.toString())
            case _ =>
                scriptSQLExecListener.addResult("explainStr", sparkSession.sql(chunks.tail.mkString(" ")).queryExecution.toString())
        }
    }
}
