package cn.i4.iql.utils

import java.net.URLDecoder

import com.alibaba.fastjson.{JSON, JSONObject}
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


  def formatetDeviceModel(spark:SparkSession) = {
    var map:Map[String,String] = Map()
    map+=("ipad1"->"iPad 1")
    map+=("ipad2"->"iPad 2")
    map+=("ipad2(32nm)"->"iPad 2(32NM)")
    map+=("ipad3"->"iPad 3")
    map+=("ipad4"->"iPad 4")
    map+=("ipadair"->"iPad Air")
    map+=("ipadair2"->"iPad Air 2")
    map+=("ipadmini"->"iPad mini")
    map+=("ipadmini2"->"iPad mini 2")
    map+=("ipadmini3"->"iPad mini 3")
    map+=("ipadmini4"->"iPad mini 4")
    map+=("ipadpro2(10.5)"->"iPad Pro 2(10.5)")
    map+=("ipadpro2(12.9)"->"iPad Pro 2(12.9)")
    map+=("ipadpro(12.9)"->"iPad Pro(12.9)")
    map+=("ipadpro(9.7)"->"iPad Pro(9.7)")
    map+=("ipad(9.7)"->"iPad(9.7)")
    map+=("iphone"->"iPhone")
    map+=("iphone3g"->"iPhone 3G")
    map+=("iphone3gs"->"iPhone 3GS")
    map+=("iphone4"->"iPhone 4")
    map+=("iphone4(reva)"->"iPhone 4(Rev A)")
    map+=("iphone4s"->"iPhone 4s")
    map+=("iphone5"->"iPhone 5")
    map+=("iphone5c"->"iPhone 5c")
    map+=("iphone5s"->"iPhone 5s")
    map+=("iphone6"->"iPhone 6")
    map+=("iphone6plus"->"iPhone 6 Plus")
    map+=("iphone6s"->"iPhone 6s")
    map+=("iphone6splus"->"iPhone 6s Plus")
    map+=("iphone7"->"iPhone 7")
    map+=("iphone7plus"->"iPhone 7 Plus")
    map+=("iphone8"->"iPhone 8")
    map+=("iphone8plus"->"iPhone 8 Plus")
    map+=("iphonese"->"iPhone SE")
    map+=("iphonex"->"iPhone X")
    map+=("ipodtouch"->"iPod touch")
    map+=("ipodtouch2"->"iPod touch 2")
    map+=("ipodtouch3"->"iPod touch 3")
    map+=("ipodtouch4"->"iPod touch 4")
    map+=("ipodtouch5"->"iPod touch 5")
    map+=("ipodtouch6"->"iPod touch 6")
    val model: Broadcast[Map[String, String]] = spark.sparkContext.broadcast(map)

    spark.udf.register("formatetDeviceModel",(a:String) => {
        val r: Option[String] = model.value.get(a.replaceAll("\r|\n|\\s","").toLowerCase)
        if(r.isEmpty) a+"(匹配不到)"
        else r.get
    })
  }


}
