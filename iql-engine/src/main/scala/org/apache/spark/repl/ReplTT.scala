package org.apache.spark.repl

import java.io.{ByteArrayOutputStream, PrintWriter}

import cn.i4.iql.{IQLSession, IqlService}
import cn.i4.iql.repl.AbstractSparkInterpreter
import org.apache.spark.{SparkConf, SparkUtils}
import org.apache.spark.sql.SparkSession

import scala.tools.nsc.Settings
import scala.tools.nsc.interpreter.{IMain, JPrintWriter, Results}

object ReplTT {

    def main(args: Array[String]): Unit = {

        val t = new ReplTT()
        t.start()
    }

}

class ReplTT {

    def start(): IMain ={
        val sparkLoop = new SparkILoop
        val settings = new Settings()
        val conf = new SparkConf()
        val rootDir = conf.getOption("spark.repl.classdir").getOrElse(SparkUtils.getLocalDir(conf))
        val outputDir = SparkUtils.createTempDir(root = rootDir, namePrefix = "repl")

        settings.processArguments(List(
            "-Yrepl-class-based",
            "-Yrepl-outdir",
            s"${outputDir.getAbsolutePath}",
            "-classpath",
            System.getProperty("java.class.path")
        ), true)
        sparkLoop.settings = settings
        sparkLoop.createInterpreter()
        val intp = sparkLoop.intp
        intp.setContextClassLoader()
        intp.initializeSynchronous()
        println("settings.outputDirs().getSingleOutput().get() : " + settings.outputDirs.getSingleOutput.get)

        intp.bind("spark", "org.apache.spark.sql.SparkSession", IqlService.createSpark(), List("""@transient"""))
//        intp.interpret(
//            """
//    @transient val spark = cn.i4.iql.IqlService.createSpark()
//    """.stripMargin)

        intp.interpret("import org.apache.spark.SparkContext._")
        intp.interpret("import spark.implicits._")
        intp.interpret("import spark.sql")
        intp.interpret("import org.apache.spark.sql.functions._")


        val withUdfString: String = Array(
            "import org.apache.spark.SparkContext._",
            "import spark.implicits._",
            "import spark.sql",
            "import org.apache.spark.sql.functions._",
            "val upper = udf((s:String) => s.toUpperCase)",
            "val df = spark.sparkContext.parallelize(Seq('interpreted WITH udf', 'foo','bar')).toDF.withColumn('UPPER', upper($'value'))",
            "df.show(false)",
            "df.createOrReplaceTempView('df')"
        ).mkString("\n").replaceAll("'", "\"")

        intp.interpret(withUdfString)
        intp
    }


}

