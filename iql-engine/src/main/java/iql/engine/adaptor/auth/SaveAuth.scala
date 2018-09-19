package iql.engine.adaptor.auth

import iql.engine.adaptor.{DslTool, IQLAuth}
import iql.engine.antlr.IQLParser._
import iql.engine.auth.{IQLAuthListener, MLSQLTable, TableAuthResult, TableType}


class SaveAuth(authProcessListener: IQLAuthListener) extends IQLAuth with DslTool {

    override def auth(ctx: SqlContext) = {
        var path = ""
        var format = ""

        (0 until ctx.getChildCount).foreach { tokenIndex =>
            ctx.getChild(tokenIndex) match {
                case s: FormatContext =>
                    format = s.getText
                case s: PathContext =>
                    path = cleanStr(s.getText)
                case _ =>
            }
        }

        val mLSQLTable = if (format == "hive") {
            val Array(db, table) = path.split("\\.")
            MLSQLTable(Some(db), Some(table), TableType.HIVE)
        } else {
            MLSQLTable(None, Some(path), TableType.from(format).get)
        }
        authProcessListener.addTable(mLSQLTable)
    }
}
