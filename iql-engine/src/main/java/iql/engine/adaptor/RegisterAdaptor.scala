package iql.engine.adaptor

import iql.engine.IQLSQLExecListener
import iql.engine.antlr.IQLParser._
import iql.engine.udf.ScalaScriptUDF

class RegisterAdaptor(scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor with DslTool {

  override def parse(ctx: SqlContext): Unit = {
    var functionName = ""
    var format = ""
    var path = ""
    var option = Map[String, String]()
    val sparkSession = scriptSQLExecListener.sparkSession
    (0 until ctx.getChildCount).foreach { tokenIndex =>

      ctx.getChild(tokenIndex) match {
        case s: FunctionNameContext =>
          functionName = s.getText
        case s: FormatContext =>
          format = s.getText
        case s: PathContext =>
          path = cleanStr(s.getText)
        case s: ExpressionContext =>
          option += (cleanStr(s.identifier().getText) -> cleanStr(s.STRING().getText))
        case s: BooleanExpressionContext =>
          option += (cleanStr(s.expression().identifier().getText) -> cleanStr(s.expression().STRING().getText))
        case _ =>
      }
    }
    format.toLowerCase match {
      case "udf" =>
        require(option.contains("func"),"Registration UDF must specify the UDF function: func")
        val func = ScalaScriptUDF.load(sparkSession,option("func"),option)
        ScalaScriptUDF.predict(sparkSession,func,path,option)
      case "watermark" =>
        require(option.contains("eventTimeCol"),"Registration watermark must specify the eventTimeCol")
        require(option.contains("delayThreshold"),"Registration watermark must specify the delayThreshold")
        sparkSession.table(path).withWatermark(option("eventTimeCol"),option("delayThreshold"))
            .createOrReplaceTempView(path)
    }
  }
}


