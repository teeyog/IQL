package cn.i4.iql

import java.util.concurrent.ConcurrentHashMap

import cn.i4.iql.adaptor._
import cn.i4.iql.antlr.{IQLBaseListener}
import cn.i4.iql.antlr.IQLParser._
import org.apache.spark.sql.SparkSession

class IQLSQLExecListener(var _sparkSession: SparkSession,_iqlSession:IQLSession) extends IQLBaseListener  with Logging {

  private val _env = new scala.collection.mutable.HashMap[String, String]
  private val _result = new ConcurrentHashMap[String, String]

  def addEnv(k: String, v: String) = {
    _env(k) = v
    this
  }

  def env() = _env

  def addResult(k: String, v: String) = {
    _result.put(k,v)
    this
  }

  def getResult(k:String) = {
    _result.getOrDefault(k, "")
  }

  def result() = _result

  def sparkSession = _sparkSession
  def iqlSession = _iqlSession


  override def exitSql(ctx: SqlContext): Unit = {
    ctx.getChild(0).getText.toLowerCase() match {
      case "load" =>
        new LoadAdaptor(this).parse(ctx)

      case "select" =>
        new SelectAdaptor(this).parse(ctx)

      case "save" =>
        new SaveAdaptor(this).parse(ctx)

      case "create" =>
        new CreateAdaptor(this).parse(ctx)

      case "insert" =>
        new InsertAdaptor(this).parse(ctx)

      case "set" =>
        new SetAdaptor(this).parse(ctx)

      case "show" =>
        new ShowAdaptor(this).parse(ctx)

      case "register" =>
        new RegisterAdaptor(this).parse(ctx)
    }
  }

  override def exitStatement(ctx: StatementContext): Unit = {
    sparkSession.catalog.listTables().collect().foreach(r => sparkSession.catalog.dropTempView(r.name))
    sparkSession.catalog.listDatabases().rdd.map(_.name).collect().foreach(d =>
      sparkSession.catalog.listTables(d).rdd.map(_.name).collect().foreach(t =>
        sparkSession.catalog.refreshTable(d + "." + t))
    )
  }

}
