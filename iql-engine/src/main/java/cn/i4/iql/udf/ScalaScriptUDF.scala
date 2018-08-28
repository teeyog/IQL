package cn.i4.iql.udf

import org.apache.spark.sql.bridge.SparkBridge
import org.apache.spark.sql.catalyst.expressions.{Expression, ScalaUDF}
import org.apache.spark.sql.expressions.UserDefinedFunction
import org.apache.spark.sql.{DataFrame, SparkSession}

object ScalaScriptUDF  {

   def load(sparkSession: SparkSession, res: String, params: Map[String, String]): Any = {
    val (func, returnType) =  ScalaSourceUDF(res, params.get("methodName"))
    (e: Seq[Expression]) => ScalaUDF(func, returnType, e)
  }

   def predict(sparkSession: SparkSession, _model: Any, name: String, params: Map[String, String]): UserDefinedFunction = {
    val func = _model.asInstanceOf[(Seq[Expression]) => ScalaUDF]
    SparkBridge.register(sparkSession, name, func)
    null
  }
}
