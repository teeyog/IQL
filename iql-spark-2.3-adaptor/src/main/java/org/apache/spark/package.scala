package org.apache.spark

import org.apache.spark.sql.execution.datasources.hbase.{DataFrameFunctions, SparkSqlContextFunctions}
import org.apache.spark.sql.{DataFrame, SparkSession}

package object spark {

    implicit def toSparkSqlContextFunctions(spark: SparkSession): SparkSqlContextFunctions = {
        new SparkSqlContextFunctions(spark)
    }

    implicit def toDataFrameFunctions(data: DataFrame): DataFrameFunctions = {
        new DataFrameFunctions(data)
    }

}
