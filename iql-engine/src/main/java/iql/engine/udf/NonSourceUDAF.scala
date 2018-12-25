package iql.engine.udf

import org.apache.spark.SparkUtils
import org.apache.spark.sql.expressions.UserDefinedAggregateFunction

/**
  * Created by UFO on 12/25/2018.
  */
object NonSourceUDAF {
    def apply(className: String): UserDefinedAggregateFunction = {
        val clazz = Class.forName(className, true,  SparkUtils.getContextOrSparkClassLoader)
        SourceCodeCompiler.newInstance(clazz).asInstanceOf[UserDefinedAggregateFunction]
    }
}