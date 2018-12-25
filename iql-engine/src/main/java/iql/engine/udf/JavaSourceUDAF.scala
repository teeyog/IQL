package iql.engine.udf


import org.apache.spark.sql.Row
import org.apache.spark.sql.expressions.{MutableAggregationBuffer, UserDefinedAggregateFunction}
import org.apache.spark.sql.types.{DataType, StructType}

import scala.reflect.ClassTag

/**
  * Created by UFO on 12/25/2018.
  */
object JavaSourceUDAF {
    def apply(src: String, className: String): UserDefinedAggregateFunction = {
        generateAggregateFunction(src, className)
    }

    private def generateAggregateFunction(src: String, className: String): UserDefinedAggregateFunction = {
        new UserDefinedAggregateFunction with Serializable {

            @transient val clazzUsingInDriver = SourceCodeCompiler.compileJava(src, className)
            @transient val instanceUsingInDriver = SourceCodeCompiler.newInstance(clazzUsingInDriver)

            lazy val clazzUsingInExecutor = SourceCodeCompiler.compileJava(src, className)
            lazy val instanceUsingInExecutor = SourceCodeCompiler.newInstance(clazzUsingInExecutor)

            def invokeMethod[T: ClassTag](clazz: Class[_], instance: Any, method: String): T = {
                SourceCodeCompiler.getMethod(clazz, method).invoke(instance).asInstanceOf[T]
            }

            val _inputSchema = invokeMethod[StructType](clazzUsingInDriver, instanceUsingInDriver, "inputSchema")
            val _dataType = invokeMethod[DataType](clazzUsingInDriver, instanceUsingInDriver, "dataType")
            val _bufferSchema = invokeMethod[StructType](clazzUsingInDriver, instanceUsingInDriver, "bufferSchema")
            val _deterministic = invokeMethod[Boolean](clazzUsingInDriver, instanceUsingInDriver, "deterministic")

            override def inputSchema: StructType = {
                _inputSchema
            }

            override def dataType: DataType = {
                _dataType
            }

            override def bufferSchema: StructType = {
                _bufferSchema
            }

            override def deterministic: Boolean = {
                _deterministic
            }

            lazy val _update = SourceCodeCompiler.getMethod(clazzUsingInExecutor, "update")
            lazy val _merge = SourceCodeCompiler.getMethod(clazzUsingInExecutor, "merge")
            lazy val _initialize = SourceCodeCompiler.getMethod(clazzUsingInExecutor, "initialize")
            lazy val _evaluate = SourceCodeCompiler.getMethod(clazzUsingInExecutor, "evaluate")

            override def update(buffer: MutableAggregationBuffer, input: Row): Unit = {
                _update.invoke(instanceUsingInExecutor, buffer, input)
            }

            override def merge(buffer1: MutableAggregationBuffer, buffer2: Row): Unit = {
                _merge.invoke(instanceUsingInExecutor, buffer1, buffer2)
            }

            override def initialize(buffer: MutableAggregationBuffer): Unit = {
                _initialize.invoke(instanceUsingInExecutor, buffer)
            }

            override def evaluate(buffer: Row): Any = {
                _evaluate.invoke(instanceUsingInExecutor, buffer)
            }


        }
    }
}