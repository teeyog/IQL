package cn.i4.iql.session

import cn.i4.iql.Logging
import org.apache.spark.sql.SparkSession

class IqlSession(@transient val spark: SparkSession) extends Serializable  with Logging{

  def reset(): Unit = {
    warn(s"reset spark session: $spark:")
    spark.sessionState.catalog.reset()
  }

  def setJobGroup(group: String, description: String): Unit = {
    warn(s"set job group id as $group, group description is $description")
    spark.sparkContext.setJobGroup(group, description, interruptOnCancel = true)
  }

  def clearJobGroup(): Unit = {
    spark.sparkContext.clearJobGroup()
  }

  def cancelJobGroup(group: String): Unit = {
    warn(s"cancel job group $group, all running and waiting jobs will be cancelled")
    spark.sparkContext.cancelJobGroup(group)
  }

}
