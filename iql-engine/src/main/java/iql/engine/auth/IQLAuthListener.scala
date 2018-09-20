package iql.engine.auth

import iql.common.Logging
import iql.engine.adaptor.auth._
import iql.engine.antlr.IQLBaseListener
import iql.engine.antlr.IQLParser._
import org.apache.spark.sql.SparkSession

import scala.collection.mutable.ArrayBuffer

class IQLAuthListener(var _sparkSession: SparkSession) extends IQLBaseListener  with Logging {

  def sparkSession = _sparkSession

  private val _tables = MLSQLTableSet(ArrayBuffer[MLSQLTable]())

  def addTable(table: MLSQLTable) = {
    _tables.tables.asInstanceOf[ArrayBuffer[MLSQLTable]] += table
  }

  def withDBs = {
    _tables.tables.filter(f => f.db.isDefined)
  }

  def withoutDBs = {
    _tables.tables.filterNot(f => f.db.isDefined)
  }

  def tables() = _tables

  override def exitSql(ctx: SqlContext): Unit = {
    ctx.getChild(0).getText.toLowerCase() match {
      case "load" =>
        new LoadAuth(this).auth(ctx)

      case "select" =>
        new SelectAuth(this).auth(ctx)

      case "save" =>
        new SaveAuth(this).auth(ctx)

      case "create" =>
        new CreateAuth(this).auth(ctx)

      case "insert" =>
        new InsertAuth(this).auth(ctx)

      case "drop" =>
        new DropAuth(this).auth(ctx)

      case _ =>
    }
  }

}
