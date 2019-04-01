package iql.engine.adaptor

import iql.engine.{ExeActor, IQLSQLExecListener}
import iql.engine.antlr.IQLLexer
import iql.engine.antlr.IQLParser.SqlContext
import iql.engine.config.IQL_AUTH_ENABLE
import iql.engine.utils.PropsUtils
import org.antlr.v4.runtime.misc.Interval
import org.apache.http.client.fluent.{Form, Request}

class ImportAdaptor(scriptSQLExecListener: IQLSQLExecListener) extends DslAdaptor {

  val authEnable = scriptSQLExecListener.sparkSession.sparkContext.getConf.getBoolean(IQL_AUTH_ENABLE.key, IQL_AUTH_ENABLE.defaultValue.get)

  override def parse(ctx: SqlContext): Unit = {
    val input = ctx.start.getTokenSource.asInstanceOf[IQLLexer]._input
    val start = ctx.start.getStartIndex
    val stop = ctx.stop.getStopIndex
    val interval = new Interval(start, stop)
    val originalText = input.getText(interval)
    val path = originalText.replace("import", "").replace("IMPORT", "").replace("include", "").replace("INCLUDE", "").trim
    val script = getScriptByPath(path)
    ExeActor.parse(script, scriptSQLExecListener, null, true)
  }

  def getScriptByPath(originalText: String): String = {
    val url = PropsUtils.get("iql.server.address") + "/jobScript/getScriptByPath"
    Request.Post(url).bodyForm(Form.form().add("packageName", originalText).
      add("token", scriptSQLExecListener.token)
      .build())
      .execute().returnContent().asString()
  }

}
