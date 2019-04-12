package org.apache.spark

import java.io.File

import org.apache.spark.util.Utils

/**
  * Created by UFO on 2019-04-02
  */
object SparkUtils {

  def getUserJars(conf: SparkConf, isShell: Boolean = false): Seq[String] = {
    val sparkJars = conf.getOption("spark.jars")
    if (isShell && conf.get("spark.master") == "yarn" ) {
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

  def getLocalUserJarsForShell(conf: SparkConf): Seq[String] = {
    Utils.getLocalUserJarsForShell(conf)
  }

  def getContextOrSparkClassLoader = Utils.getContextOrSparkClassLoader

}
