package org.apache.spark.sql.bridge

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

}
