package cn.i4.iql.adaptor

import java.util.concurrent.ConcurrentHashMap

import cn.i4.iql.IQLSQLExecListener
import cn.i4.iql.antlr.IQLLexer
import cn.i4.iql.antlr.IQLParser.{SqlContext, TableNameContext}
import org.antlr.v4.runtime.misc.Interval

class SelectAdaptor(scriptSQLExecListener: IQLSQLExecListener, resultMap:ConcurrentHashMap[String, String]) extends DslAdaptor {
  override def parse(ctx: SqlContext): Unit = {
    val input = ctx.start.getTokenSource().asInstanceOf[IQLLexer]._input

    val start = ctx.start.getStartIndex()
    val stop = ctx.stop.getStopIndex()
    val interval = new Interval(start, stop)
    val originalText = input.getText(interval)
    var tableName = ""
    //    (0 to ctx.getChildCount() - 1).foreach { tokenIndex =>
//      ctx.getChild(tokenIndex) match {
//        case s: TableNameContext =>
//          tableName = s.getText
//        case _ =>
//      }
//    }

    val chunks = originalText.split("\\s+")
    if(chunks(chunks.length - 2).equals("as")) tableName = chunks.last.replace(";", "")
    val sql = originalText.replaceAll(s"as[\\s|\\n]+${tableName}", "")
    if(tableName.equals("")){
      val hdfsPath = "/tmp/iql/result/iql_query_result_" + System.currentTimeMillis()
      val df_result = scriptSQLExecListener.sparkSession.sql(sql)
      resultMap.put("schema",df_result.schema.fields.map(_.name).mkString(","))
      df_result.write.option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ").json(hdfsPath)
      resultMap.put("hdfsPath",hdfsPath)
    } else {
      scriptSQLExecListener.sparkSession.sql(sql).createOrReplaceTempView(tableName)
    }

  }
}
