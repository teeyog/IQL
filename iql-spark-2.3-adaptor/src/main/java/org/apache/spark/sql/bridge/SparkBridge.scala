package org.apache.spark.sql.bridge

import java.io.CharArrayWriter

import org.apache.spark.sql.catalyst.FunctionIdentifier
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry.FunctionBuilder
import org.apache.spark.sql.catalyst.catalog.ExternalCatalog
import org.apache.spark.sql.catalyst.encoders.RowEncoder
import org.apache.spark.sql.catalyst.expressions.{Expression, ScalaUDF}
import org.apache.spark.sql.catalyst.json.{JSONOptions, JacksonGenerator}
import org.apache.spark.sql.types.DataType
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}

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

  def getHiveCatalg(sparkSession:SparkSession): ExternalCatalog = {
    sparkSession.sharedState.externalCatalog.asInstanceOf[ExternalCatalog]
  }

  def register(sparkSession: SparkSession, name: String, udf: FunctionBuilder) = {
    sparkSession.sessionState.functionRegistry.registerFunction(FunctionIdentifier(name), udf)
  }

  def toJson(dataSet: DataFrame) = {
    val rowSchema = dataSet.schema
    val sessionLocalTimeZone = dataSet.sparkSession.sessionState.conf.sessionLocalTimeZone

    val writer = new CharArrayWriter()
    // create the Generator without separator inserted between 2 records
    val gen = new JacksonGenerator(rowSchema, writer,
      new JSONOptions(Map.empty[String, String], sessionLocalTimeZone))

    val enconder = RowEncoder.apply(rowSchema).resolveAndBind()
    val res = dataSet.collect.map { row =>
      gen.write(enconder.toRow(row))
      gen.flush()
      val json = writer.toString
      writer.reset()
      json
    }
    gen.close()
    res
  }

}

class IQLScalaUDF(override val function: AnyRef,
                       override val dataType: DataType,
                       override val children: Seq[Expression],
                       override val inputTypes: Seq[DataType] = Nil,
                       override val udfName: Option[String] = None,
                       override val nullable: Boolean = true,
                       override val udfDeterministic: Boolean = true)
  extends ScalaUDF(function, dataType, children, inputTypes, udfName, nullable, udfDeterministic)