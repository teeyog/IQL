package iql.common.utils

import java.net.URI
import java.util.Properties

import org.apache.hadoop.conf.Configuration
import org.apache.hadoop.fs.{FileSystem, Path}

import scala.collection.JavaConverters._

object PropsUtils {

    //  val confMap:Map[String,String] = getPropertiesFromHDFSFile("hdfs://dsj01:8020/data/resource/iql-default.properties")
    val confMap: Map[String, String] = getPropertiesFromLocalFile("iql.properties")

    def get(k: String): String = {
        confMap(k)
    }

    /**
      * load config file from hdfs
      */
    def getPropertiesFromHDFSFile(path: String): Map[String, String] = {
        val pt = new Path(path)
        try {
            val fs = FileSystem.get(URI.create(confMap("hdfs.url")), new Configuration())
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

    /**
      * load config file from hdfs
      */
    def getPropertiesFromLocalFile(fileName: String): Map[String, String] = {
        try {
            val inputStream = PropsUtils.getClass.getClassLoader.getResourceAsStream(fileName)
            val properties = new Properties()
            properties.load(inputStream)
            properties.stringPropertyNames().asScala.map(
                k => (k, properties.getProperty(k).trim)
            ).toMap
        } catch {
            case e: Exception =>
                throw new Exception(s"Failed when loading iql properties from $fileName", e)
        }
    }

}
