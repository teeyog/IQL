package iql.engine.auth

import java.util.concurrent.atomic.AtomicReference

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.TableIdentifier

import scala.collection.mutable.ArrayBuffer

object IQLAuthParser {
  val parser = new AtomicReference[IQLSparkSqlParser]()

  def filterTables(sql: String, session: SparkSession) = {
    val t = ArrayBuffer[TableIdentifier]()
    lazy val parserInstance = new IQLSparkSqlParser(session.sessionState.conf)
    parser.compareAndSet(null, parserInstance)
    parser.get().tables(sql, t)
    t
  }
}
