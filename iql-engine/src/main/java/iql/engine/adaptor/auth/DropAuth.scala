package iql.engine.adaptor.auth

import iql.engine.adaptor.{DslTool, IQLAuth}
import iql.engine.antlr.IQLLexer
import iql.engine.antlr.IQLParser.SqlContext
import iql.engine.auth._
import org.antlr.v4.runtime.misc.Interval

class DropAuth(authProcessListener: IQLAuthListener) extends IQLAuth with DslTool {

  override def auth(ctx: SqlContext) = {
    val input = ctx.start.getTokenSource.asInstanceOf[IQLLexer]._input

    val start = ctx.start.getStartIndex
    val stop = ctx.stop.getStopIndex
    val interval = new Interval(start, stop)
    val originalText = input.getText(interval)


    val tableRefs = IQLAuthParser.filterTables(originalText, authProcessListener.sparkSession)

    tableRefs.foreach { f =>
      f.database match {
        case Some(db) =>
          val exists = authProcessListener.withDBs.exists(m => f.table == m.table.get && db == m.db.get)
          if (!exists) {
            authProcessListener.addTable(MLSQLTable(Some(db), Some(f.table), TableType.HIVE))
          }
        case None =>
          val exists = authProcessListener.withoutDBs.exists(m => f.table == m.table.get)
          if (!exists) {
            authProcessListener.addTable(MLSQLTable(None, Some(f.table), TableType.TEMP))
          }
      }
    }
  }
}
