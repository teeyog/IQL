package org.apache.spark.sql.bridge

import org.apache.spark.sql.catalyst.analysis.FunctionRegistry.FunctionBuilder
import org.apache.spark.sql.catalyst.expressions.{Expression, ScalaUDF}
import org.apache.spark.sql.hive.HiveExternalCatalog
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.{Dataset, Row, SparkSession}

/**
  * Created by UFO on 2019-04-02
  */
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

  def register(sparkSession: SparkSession, name: String, udf: FunctionBuilder) = {
    sparkSession.sessionState.functionRegistry.registerFunction(name, udf)
  }

}

class IQLScalaUDF(override val function: AnyRef,
                  override val dataType: DataType,
                  override val children: Seq[Expression],
                  override val inputTypes: Seq[DataType] = Nil,
                  override val udfName: Option[String] = None)
  extends ScalaUDF(function, dataType, children, inputTypes, udfName)
