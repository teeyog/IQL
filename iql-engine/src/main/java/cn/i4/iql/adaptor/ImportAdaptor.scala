package cn.i4.iql.adaptor

import java.util

import cn.i4.iql.{ExeActor, IQLSQLExecListener}
import cn.i4.iql.antlr.IQLLexer
import cn.i4.iql.antlr.IQLParser.SqlContext
import cn.i4.iql.utils.{HttpUtils, PropsUtils}
import org.antlr.v4.runtime.misc.Interval

class ImportAdaptor(scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor {

  override def parse(ctx: SqlContext): Unit = {
    val input = ctx.start.getTokenSource.asInstanceOf[IQLLexer]._input
    val start = ctx.start.getStartIndex
    val stop = ctx.stop.getStopIndex
    val interval = new Interval(start, stop)
    val originalText = input.getText(interval)
    val path = originalText.replace("import","").replace("IMPORT","").replace("include","").replace("INCLUDE","").trim
    val script = getScriptByPath(path)
    ExeActor.parse(script,scriptSQLExecListener)
  }

  def getScriptByPath(originalText:String): String ={
    val pramsMap = new util.HashMap[String,String]()
    pramsMap.put("packageName",originalText)
    val url = PropsUtils.get("iql.server.adress") + "/jobScript/getScriptByPath"
    HttpUtils.get(url,pramsMap,5,"utf-8")
  }

}
