package cn.i4.iql.utils

import java.util.Properties
import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.FileSystem
import org.apache.hadoop.fs.Path
import scala.collection.JavaConverters._

object PropsUtils {

  val confMap:Map[String,String] = getPropertiesFromHDFSFile("hdfs://dsj01:8020/data/resource/iql-default.properties")

  def get(k:String):String = {
    confMap(k)
  }

  /**
    * load config file from hdfs
    * @param path:path
    * @return
    */
  def getPropertiesFromHDFSFile(path: String): Map[String, String] = {
    val pt = new Path(path)
    try {
      val fs = FileSystem.get(new Configuration())
      val currencyInputStream = fs.open(pt)
      val properties = new Properties()
      properties.load(currencyInputStream)
      properties.stringPropertyNames().asScala.map(
        k => (k, properties.getProperty(k).trim)
      ).toMap
    } catch {
      case e: Exception =>
        throw new Exception(s"Failed when loading iql properties from $path", e)
    }
  }

}
