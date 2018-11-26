package iql.engine.adaptor

import java.util.UUID

import iql.engine.IQLSQLExecListener
import iql.engine.antlr.IQLLexer
import iql.engine.antlr.IQLParser.SqlContext
import org.antlr.v4.runtime.misc.Interval

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
      val uuidTable = UUID.randomUUID().toString.replace("-","")
      scriptSQLExecListener.addResult("uuidTable", uuidTable)
      sparkSession.sql(originalText).createOrReplaceTempView(uuidTable)
    } else {
      sparkSession.sql(sql).createOrReplaceTempView(tableName)
    }

  }
}
