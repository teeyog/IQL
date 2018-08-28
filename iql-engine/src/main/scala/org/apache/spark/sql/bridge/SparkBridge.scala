package org.apache.spark.sql.bridge

import org.apache.spark.sql.catalyst.FunctionIdentifier
import org.apache.spark.sql.catalyst.expressions.{Expression, ScalaUDF}
import org.apache.spark.sql.hive.HiveExternalCatalog
import org.apache.spark.sql.{Dataset, Row, SparkSession}

object SparkBridge {

  def showString(dataset:Dataset[Row],numRows:Int,truncate:Boolean): String = {
    if (truncate) {
      dataset.showString(numRows, truncate = 20)
    } else {
      dataset.showString(numRows, truncate = 0)
    }
  }

  def getHiveCatalg(sparkSession:SparkSession): HiveExternalCatalog = {
    sparkSession.sharedState.externalCatalog.asInstanceOf[HiveExternalCatalog]
  }

  def register(sparkSession: SparkSession, name: String, udf: (Seq[Expression]) => ScalaUDF) = {
    sparkSession.sessionState.functionRegistry.registerFunction(FunctionIdentifier(name), udf)
  }

}
