package org.apache.spark

import java.io.File

import org.apache.spark.util.Utils
import org.apache.spark.util.Utils.unionFileLists

object SparkUtils {

  def getUserJars(conf: SparkConf, isShell: Boolean = false): Seq[String] = {
    val sparkJars = conf.getOption("spark.jars")
    if (conf.get("spark.master") == "yarn" && isShell) {
      val yarnJars = conf.getOption("spark.yarn.dist.jars")
      unionFileLists(sparkJars, yarnJars).toSeq
    } else {
      sparkJars.map(_.split(",")).map(_.filter(_.nonEmpty)).toSeq.flatten
    }
  }

  def getLocalDir(conf: SparkConf): String = {
    Utils.getLocalDir(conf)
  }

  def createTempDir(
                     root: String = System.getProperty("java.io.tmpdir"),
                     namePrefix: String = "spark"): File = {
    Utils.createTempDir(root,namePrefix)
  }

  def unionFileLists(leftList: Option[String], rightList: Option[String]): Set[String] = {
    var allFiles = Set[String]()
    leftList.foreach { value => allFiles ++= value.split(",") }
    rightList.foreach { value => allFiles ++= value.split(",") }
    allFiles.filter { _.nonEmpty }
  }

}
