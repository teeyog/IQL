package cn.i4.iql.adaptor

import cn.i4.iql.IQLSQLExecListener
import cn.i4.iql.IQLSQLExecListener
import cn.i4.iql.antlr.IQLParser._
import org.apache.spark.sql._


class RegisterAdaptor (scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor {
  override def parse(ctx: SqlContext): Unit = {
    var functionName = ""
    var format = ""
    var path = ""
    (0 until ctx.getChildCount).foreach { tokenIndex =>

      ctx.getChild(tokenIndex) match {
        case s: FunctionNameContext =>
          functionName = s.getText
        case s: FormatContext =>
          format = s.getText
        case s: PathContext =>
          path = withPathPrefix(scriptSQLExecListener.pathPrefix, cleanStr(s.getText))
        case _ =>
      }
    }
//    val alg = MLMapping.findAlg(format)
//    val sparkSession = scriptSQLExecListener.sparkSession
//    val model = alg.load(sparkSession, path)
//    val udf = alg.predict(sparkSession, model, functionName)
    val udf = (arg1:String) => arg1
    scriptSQLExecListener.sparkSession.udf.register(functionName, udf)
  }
}
