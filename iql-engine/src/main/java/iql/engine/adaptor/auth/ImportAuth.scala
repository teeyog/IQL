package iql.engine.adaptor.auth

import iql.engine.ExeActor
import iql.engine.adaptor.{DslTool, IQLAuth}
import iql.engine.antlr.IQLLexer
import iql.engine.antlr.IQLParser.SqlContext
import iql.engine.auth._
import iql.engine.utils.PropsUtils
import org.antlr.v4.runtime.misc.Interval
import org.apache.http.client.fluent.{Form, Request}


class ImportAuth(authListener: IQLAuthListener) extends IQLAuth with DslTool {

    override def auth(ctx: SqlContext): Unit = {
        val input = ctx.start.getTokenSource.asInstanceOf[IQLLexer]._input
        val start = ctx.start.getStartIndex
        val stop = ctx.stop.getStopIndex
        val interval = new Interval(start, stop)
        val originalText = input.getText(interval)
        val path = originalText.replace("import", "").replace("IMPORT", "").replace("include", "").replace("INCLUDE", "").trim
        ExeActor.checkAuth(getScriptByPath(path),Some(authListener))
    }

    def getScriptByPath(originalText: String): String = {
        val url = PropsUtils.get("iql.server.address") + "/jobScript/getScriptByPath"
        Request.Post(url).bodyForm(Form.form().add("packageName", originalText).
            add("token", PropsUtils.get("token"))
            .build())
            .execute().returnContent().asString()
    }
}
