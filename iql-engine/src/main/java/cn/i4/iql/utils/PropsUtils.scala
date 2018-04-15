package cn.i4.iql.utils

import java.io.{IOException, InputStream}
import java.util.Properties

object PropsUtils {

  val props = getProps("default.properties")

  /**
    * 加载配置文件
    *
    * @param fileName
    * @return
    */
  private def getProps(fileName: String) = {
    val pros = new Properties
    val inputStream = Thread.currentThread().getContextClassLoader.getResourceAsStream(fileName)
    try{
      pros.load(inputStream)
    }
    catch {
      case e: IOException =>
        e.printStackTrace()
    } finally if (inputStream != null) try
      inputStream.close()
    catch {
      case e: IOException =>
        e.printStackTrace()
    }
    pros
  }

}
