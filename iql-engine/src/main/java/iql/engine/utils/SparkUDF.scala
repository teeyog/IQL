package iql.engine.utils

import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.SparkSession

/**
 * 常用Spark UDF
 */
object SparkUDF {

  /**
   * 过滤非数字字符串
   */
  def filterNumStr(spark:SparkSession) = {
    spark.udf.register("filterNumStr",(s: String) => {
      if(s.indexOf("pro-")>=0){
        s.trim
      } else if (s == null || (!s.matches("^[0-9]+$")) || s.length>10) {
        "-1"
      } else {
        s.trim
      }
    })
  }

  /**
    * 根据正则过滤字符串
    * 不符合时可自定义返回值
    */
  def filterStrByFmtDefRetn(spark:SparkSession) = {
    spark.udf.register("filterStrByFmtDefRetn",(s: String,reg:String,retn:String) => {
      if (s == null || (!s.matches(reg))) {
        retn
      } else {
        s.trim
      }
    })
  }

  /**
   * 格式化model
   */
  def getModel(spark:SparkSession) = {
    spark.udf.register("getModel",(a:String) => {
      if(a.toLowerCase.startsWith("ipho")) 1
      else if(a.toLowerCase.startsWith("ipad")) 2
      else 3
    })
  }



  /**
    * 字符串转数组
    */
  def str2array(spark:SparkSession) = {
    spark.udf.register("str2array",(data:String) => {
      val array = com.alibaba.fastjson.JSON.parseArray(data)
      val value = array.iterator()
      var rList:List[String] = List()
      while (value.hasNext){
        rList ::= value.next().toString
      }
      rList
    })
  }

}
