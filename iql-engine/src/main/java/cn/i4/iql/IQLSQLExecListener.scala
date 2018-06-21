package cn.i4.iql

import java.util.concurrent.ConcurrentHashMap

import cn.i4.iql.adaptor._
import cn.i4.iql.antlr.{IQLBaseListener}
import cn.i4.iql.antlr.IQLParser._
import org.apache.spark.sql.SparkSession

class IQLSQLExecListener(var _sparkSession: SparkSession, _pathPrefix: String, resultMap:ConcurrentHashMap[String, String]) extends IQLBaseListener  with Logging {
  def sparkSession = _sparkSession
  def pathPrefix: String = {
    if (_pathPrefix == null || _pathPrefix.isEmpty) return ""
    if (!_pathPrefix.endsWith("/")) {
      return _pathPrefix + "/"
    }
    return _pathPrefix
  }

  override def exitSql(ctx: SqlContext): Unit = {
    ctx.getChild(0).getText.toLowerCase() match {
      case "load" =>
        new LoadAdaptor(this).parse(ctx)

      case "select" =>
        new SelectAdaptor(this,resultMap).parse(ctx)

      case "save" =>
        new SaveAdaptor(this).parse(ctx)

      case "create" =>
        new CreateAdaptor(this).parse(ctx)

      case "insert" =>
        new InsertAdaptor(this).parse(ctx)

      case "set" =>
        new SetAdaptor(this).parse(ctx)

      case "show" =>
        new ShowAdaptor(this,resultMap).parse(ctx)
    }

  }

  override def exitStatement(ctx: StatementContext): Unit = {
//    sparkSession.catalog.listTables().collect().foreach(r => sparkSession.catalog.dropTempView(r.name))
  }

  override def enterStatement(ctx: StatementContext): Unit = {
    //刷新元数据
//    sparkSession.catalog.listDatabases().rdd.map(_.name).collect().foreach(d =>
//      sparkSession.catalog.listTables(d).rdd.map(_.name).collect().foreach(t =>
//        sparkSession.catalog.refreshTable(d + "." + t))
//    )
    reset()
  }

  def reset(): Unit = {
    warn(s"reset sparkSession: $sparkSession")
    sparkSession.sessionState.catalog.reset()
  }

}
