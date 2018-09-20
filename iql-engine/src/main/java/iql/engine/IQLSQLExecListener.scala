package iql.engine

import java.util.concurrent.ConcurrentHashMap

import iql.common.Logging
import iql.engine.adaptor._
import iql.engine.antlr.IQLBaseListener
import iql.engine.antlr.IQLParser._
import iql.engine.auth.IQLAuthListener
import org.apache.spark.sql.SparkSession

class IQLSQLExecListener(var _sparkSession: SparkSession,_iqlSession:IQLSession) extends IQLBaseListener  with Logging {

  private val _env = new scala.collection.mutable.HashMap[String, String]
  private val _result = new ConcurrentHashMap[String, String]
  private var _authListener:Option[IQLAuthListener] = _

  def authListener() = _authListener

  def addAuthListener(listener:Option[IQLAuthListener]) = {
    _authListener = listener
  }

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

      case "import" | "include" =>
        new ImportAdaptor(this).parse(ctx)

      case "drop" =>
        new DropAdaptor(this).parse(ctx)

      case "refresh" =>
        new RefreshAdaptor(this).parse(ctx)
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
