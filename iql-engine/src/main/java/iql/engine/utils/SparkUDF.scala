package iql.engine.utils

import org.apache.spark.sql.SparkSession

/**
 * 常用Spark UDF
 */
object SparkUDF {

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
