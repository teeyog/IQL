package cn.i4.iql.adaptor

import java.util.concurrent.ConcurrentHashMap

import cn.i4.iql.IQLSQLExecListener
import cn.i4.iql.antlr.IQLParser._
import org.apache.spark.sql._
import org.apache.spark.sql.bridge.SparkBridge

//show t where limit
class ShowAdaptor(scriptSQLExecListener: IQLSQLExecListener, resultMap:ConcurrentHashMap[String, String]) extends DslAdaptor {
  override def parse(ctx: SqlContext): Unit = {
    var oldDF: DataFrame = null
    var numRow:Int = 20
    var truncate:Boolean = true
    var option = Map[String, String]()

    (0 to ctx.getChildCount() - 1).foreach { tokenIndex =>
      ctx.getChild(tokenIndex) match {
        case s: TableNameContext =>
          oldDF = scriptSQLExecListener.sparkSession.table(s.getText)
        case s: ExpressionContext =>
          option += (cleanStr(s.identifier().getText) -> cleanStr(s.STRING().getText))
        case s: BooleanExpressionContext =>
          option += (cleanStr(s.expression().identifier().getText) -> cleanStr(s.expression().STRING().getText))
        case _ =>
      }
    }
    numRow = option.getOrElse("limit",numRow).toString.toInt
    truncate = option.getOrElse("truncate",truncate).toString.toBoolean
    resultMap.put("showResult",resultMap.getOrDefault("showResult","") + SparkBridge.showString(oldDF,numRow,truncate) + "\n\n")

  }
}
