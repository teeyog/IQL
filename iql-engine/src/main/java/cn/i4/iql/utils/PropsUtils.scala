package cn.i4.iql.utils

import java.io._
import java.nio.charset.StandardCharsets
import java.util.Properties
import scala.collection.JavaConverters._

object PropsUtils {

  def getPropertiesFromFile(filename: String): Map[String, String] = {
    val file = new File(filename)
    require(file.exists(), s"Properties file $file does not exist")
    require(file.isFile(), s"Properties file $file is not a normal file")

    val inReader: InputStreamReader = new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8)
    try {
      val properties = new Properties()
      properties.load(inReader)
      properties.stringPropertyNames().asScala.map(
        k => (k, properties.getProperty(k).trim)
      ).toMap
    } catch {
      case e: IOException =>
        throw new Exception(s"Failed when loading iql properties from $filename", e)
    } finally {
      inReader.close()
    }
  }

}
