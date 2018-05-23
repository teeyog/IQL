package cn.i4.iql.adaptor

import java.util.concurrent.ConcurrentHashMap

import cn.i4.iql.IQLSQLExecListener
import cn.i4.iql.antlr.IQLLexer
import cn.i4.iql.antlr.IQLParser._
import org.antlr.v4.runtime.misc.Interval

//show create table ods.ods_user_mbl
class ShowAdaptor(scriptSQLExecListener: IQLSQLExecListener, resultMap: ConcurrentHashMap[String, String]) extends DslAdaptor {
  override def parse(ctx: SqlContext): Unit = {
    val input = ctx.start.getTokenSource.asInstanceOf[IQLLexer]._input
    val start = ctx.start.getStartIndex
    val stop = ctx.stop.getStopIndex
    val interval = new Interval(start, stop)
    val sql = input.getText(interval)

    val hdfsPath = "/tmp/iql/result/iql_query_result_" + System.currentTimeMillis()
    val df_result = scriptSQLExecListener.sparkSession.sql(sql)
    resultMap.put("schema", df_result.schema.fields.map(_.name).mkString(","))
    df_result.write.option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ").json(hdfsPath)
    resultMap.put("hdfsPath", hdfsPath)
  }
}
