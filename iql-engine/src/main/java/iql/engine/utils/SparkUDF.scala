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
    * 格式化model
    */
  def formatetDeviceModel(spark:SparkSession) = {
    var modelmap:Map[String,String] = Map()
    modelmap+=("ipad1"->"iPad 1")
    modelmap+=("ipad2"->"iPad 2")
    modelmap+=("ipad2(32nm)"->"iPad 2(32NM)")
    modelmap+=("ipad3"->"iPad 3")
    modelmap+=("ipad4"->"iPad 4")
    modelmap+=("ipadair"->"iPad Air")
    modelmap+=("ipadair2"->"iPad Air 2")
    modelmap+=("ipadmini"->"iPad mini")
    modelmap+=("ipadmini2"->"iPad mini 2")
    modelmap+=("ipadmini3"->"iPad mini 3")
    modelmap+=("ipadmini4"->"iPad mini 4")
    modelmap+=("ipadpro2(10.5)"->"iPad Pro 2(10.5)")
    modelmap+=("ipadpro2(12.9)"->"iPad Pro 2(12.9)")
    modelmap+=("ipadpro(12.9)"->"iPad Pro(12.9)")
    modelmap+=("ipadpro(9.7)"->"iPad Pro(9.7)")
    modelmap+=("ipad(9.7)"->"iPad(9.7)")
    modelmap+=("iphone"->"iPhone")
    modelmap+=("iphone3g"->"iPhone 3G")
    modelmap+=("iphone3gs"->"iPhone 3GS")
    modelmap+=("iphone4"->"iPhone 4")
    modelmap+=("iphone4(reva)"->"iPhone 4(Rev A)")
    modelmap+=("iphone4s"->"iPhone 4s")
    modelmap+=("iphone5"->"iPhone 5")
    modelmap+=("iphone5c"->"iPhone 5c")
    modelmap+=("iphone5s"->"iPhone 5s")
    modelmap+=("iphone6"->"iPhone 6")
    modelmap+=("iphone6plus"->"iPhone 6 Plus")
    modelmap+=("iphone6s"->"iPhone 6s")
    modelmap+=("iphone6splus"->"iPhone 6s Plus")
    modelmap+=("iphone7"->"iPhone 7")
    modelmap+=("iphone7plus"->"iPhone 7 Plus")
    modelmap+=("iphone8"->"iPhone 8")
    modelmap+=("iphone8plus"->"iPhone 8 Plus")
    modelmap+=("iphonese"->"iPhone SE")
    modelmap+=("iphonex"->"iPhone X")
    modelmap+=("ipodtouch"->"iPod touch")
    modelmap+=("ipodtouch2"->"iPod touch 2")
    modelmap+=("ipodtouch3"->"iPod touch 3")
    modelmap+=("ipodtouch4"->"iPod touch 4")
    modelmap+=("ipodtouch5"->"iPod touch 5")
    modelmap+=("ipodtouch6"->"iPod touch 6")
    val br_model: Broadcast[Map[String, String]] = spark.sparkContext.broadcast(modelmap)
    spark.udf.register("formatetDeviceModel",(a:String) =>  br_model.value.get(a.replaceAll("\r|\n|\\s","").toLowerCase).getOrElse("-1"))
  }

}
