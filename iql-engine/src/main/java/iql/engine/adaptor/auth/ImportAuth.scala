package iql.engine.adaptor.auth

import java.util

import iql.common.utils.HttpUtils
import iql.engine.{ExeActor, IQLSQLExecListener}
import iql.engine.adaptor.{DslTool, IQLAuth}
import iql.engine.antlr.IQLLexer
import iql.engine.antlr.IQLParser.SqlContext
import iql.engine.auth._
import iql.engine.config.IQL_AUTH_ENABLE
import iql.engine.utils.PropsUtils
import org.antlr.v4.runtime.misc.Interval


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
        val pramsMap = new util.HashMap[String, String]()
        pramsMap.put("packageName", originalText)
        pramsMap.put("token", "fa39e32c09332d47f6f38d9c946cfa25")
        val url = PropsUtils.get("iql.server.address") + "/jobScript/getScriptByPath"
        HttpUtils.get(url, pramsMap, 5, "utf-8")
    }
}
