package iql.engine.udf

import java.util.UUID

import org.apache.spark.SparkUtils
import org.apache.spark.sql.catalyst.analysis.FunctionRegistry.FunctionBuilder
import org.apache.spark.sql.catalyst.expressions.{Expression, ScalaUDF}
import org.apache.spark.sql.execution.aggregate.ScalaUDAF
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction

/**
  * Created by UFO on 12/25/2018. 
  */
object UdfUtils {

    def wrapClass(function: String) = {
        val className = s"IQLUDF_${UUID.randomUUID().toString.replaceAll("-", "")}"
        val newfun =
            s"""
               |class  ${className}{
               |${function}
               |}
            """.stripMargin
        (className, newfun)
    }

    def javaSourceFunctionBuilder(udfName: String, src: String, className: String, methodName: Option[String]): FunctionBuilder = {
        val clazz = SourceCodeCompiler.compileJava(src, className)
        val superClassName = clazz.getSuperclass.getTypeName
        if (superClassName.equals(classOf[UserDefinedAggregateFunction].getName)) { // java udaf
            (e: Seq[Expression]) => ScalaUDAF(e, JavaSourceUDAF(src, className))
        } else { // java udf
            val (func, returnType) = JavaSourceUDF(src, className, methodName)
            (e: Seq[Expression]) => ScalaUDF(func, returnType, e, udfName = Some(udfName))
        }
    }

    def scalaSourceFunctionBuilder(udfName: String, function: String, methodName: Option[String]): FunctionBuilder = {
        val (className, src) = wrapClass(function)
        val clazz = SourceCodeCompiler.compileScala(SourceCodeCompiler.prepareScala(src, className))
        val superClassName = clazz.getSuperclass.getTypeName
        if (superClassName.equals(classOf[UserDefinedAggregateFunction].getName)) { // scala udaf
            (e: Seq[Expression]) => ScalaUDAF(e, ScalaSourceUDAF(src, className))
        } else { // scala udf
            val (func, returnType) = ScalaSourceUDF(src, className, methodName)
            (e: Seq[Expression]) => ScalaUDF(func, returnType, e, udfName = Some(udfName))
        }
    }

    def nonSourceFunctionBuilder(udfName: String, className: String, methodName: Option[String]): FunctionBuilder = {
        val clazz = Class.forName(className, true, SparkUtils.getContextOrSparkClassLoader)
        val superClassName = clazz.getSuperclass.getTypeName
        if (superClassName.equals(classOf[UserDefinedAggregateFunction].getName)) { // udaf
            (e: Seq[Expression]) => ScalaUDAF(e, NonSourceUDAF(className))
        } else { // udf
            val (func, returnType) = NonSourceUDF(className, methodName)
            (e: Seq[Expression]) => ScalaUDF(func, returnType, e, udfName = Some(udfName))
        }
    }
}
