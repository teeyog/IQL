package iql.engine.adaptor.auth

import iql.engine.adaptor.{DslTool, IQLAuth}
import iql.engine.antlr.IQLParser._
import iql.engine.auth.{IQLAuthListener, MLSQLTable, TableAuthResult, TableType}

class LoadAuth(authProcessListener: IQLAuthListener) extends IQLAuth with DslTool {

  override def auth(ctx: SqlContext) = {
    var format = ""
    var path = ""
    var tableName = ""
    (0 until ctx.getChildCount).foreach { tokenIndex =>
      ctx.getChild(tokenIndex) match {
        case s: FormatContext =>
          format = s.getText
        case s: PathContext =>
          path = s.getText
        case s: TableNameContext =>
          tableName = s.getText
        case _ =>
      }
    }

    val mLSQLTable = MLSQLTable(None, Some(cleanStr(path)), TableType.from(format).get)
    authProcessListener.addTable(mLSQLTable)

    authProcessListener.addTable(MLSQLTable(None, Some(cleanStr(tableName)), TableType.TEMP))
  }
}
