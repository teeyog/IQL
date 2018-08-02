package cn.i4.iql.adaptor

import java.util.concurrent.ConcurrentHashMap
import cn.i4.iql.IQLSQLExecListener
import cn.i4.iql.antlr.IQLParser._

class ConnectAdaptor(scriptSQLExecListener: IQLSQLExecListener, dbMap: ConcurrentHashMap[String, Map[String, String]]) extends DslAdaptor {
  override def parse(ctx: SqlContext): Unit = {
    var option = Map[String, String]()
    (0 until ctx.getChildCount).foreach { tokenIndex =>
      ctx.getChild(tokenIndex) match {
        case s: FormatContext =>
          option += ("format" -> s.getText)
        case s: ExpressionContext =>
          option += (cleanStr(s.identifier().getText) -> cleanStr(s.STRING().getText))
        case s: BooleanExpressionContext =>
          option += (cleanStr(s.expression().identifier().getText) -> cleanStr(s.expression().STRING().getText))
        case s: DbContext =>
          dbMap.put(s.getText, option)
        case _ =>
      }
    }
  }
}