package cn.i4.iql.repl

import java.io.File
import org.apache.spark.sql.SparkSession
import org.apache.spark.SparkConf

object UdfTest {
    val spark = SparkSession.builder()
            .appName("udftest")
            .master("local")
            .getOrCreate()
    import spark.implicits._

    def notInterpreted() {
        import org.apache.spark.sql._
        import org.apache.spark.sql.functions._

        val upper: String => String = _.toUpperCase
        val upperUDF = udf(upper)
        val df = spark.sparkContext.parallelize(Seq("not interpreted","foo","bar")).toDF.withColumn("UPPER", upperUDF($"value"))
        df.show()
    }

    val withoutUdfString:String = Array(
            "import spark.implicits._",
            "val df = spark.sparkContext.parallelize(Seq('interpreted without udf', 'foo','bar')).toDF",
            "df.show(false)",
            "df.createOrReplaceTempView('df')"
        ).mkString("\n").replaceAll("'", "\"")

    val withUdfString:String = Array(
            "import org.apache.spark.SparkContext._",
            "import spark.implicits._",
            "import spark.sql",
            "import org.apache.spark.sql.functions._",
            "val upper = udf((s:String) => s.toUpperCase)",
            "val df = spark.sparkContext.parallelize(Seq('interpreted WITH udf', 'foo','bar')).toDF.withColumn('UPPER', upper($'value'))",
            "df.show(false)",
            "df.createOrReplaceTempView('df')"
        ).mkString("\n").replaceAll("'", "\"")

    def main(args: Array[String]) {
        notInterpreted()
        interpret(withoutUdfString)
        interpret(withUdfString)
        spark.stop()
    }

    def interpret(script:String) {
        import scala.tools.nsc.GenericRunnerSettings
        import scala.tools.nsc.interpreter.IMain

        val cl = ClassLoader.getSystemClassLoader
        val conf = new SparkConf()
        val settings = new GenericRunnerSettings( println _ )
        val jars = (getUserJars(conf, isShell = true) ++ cl.asInstanceOf[java.net.URLClassLoader].getURLs.map(_.toString)).mkString(File.pathSeparator)
        val interpArguments = List(
            "-classpath", jars
        )
        settings.processArguments(interpArguments, true)
        settings.usejavacp.value = true

        val intp = new IMain(settings, new java.io.PrintWriter(System.out))
        intp.setContextClassLoader
        intp.initializeSynchronous

        intp.bind("spark", spark)
        intp.interpret(script)
    }

    def getUserJars(conf: SparkConf, isShell: Boolean = false): Seq[String] = {
        val sparkJars = conf.getOption("spark.jars")
//        if (conf.get("spark.master") == "yarn" && isShell) {
//            val yarnJars = conf.getOption("spark.yarn.dist.jars")
//            unionFileLists(sparkJars, yarnJars).toSeq
//        } else {
            sparkJars.map(_.split(",")).map(_.filter(_.nonEmpty)).toSeq.flatten
//        }
    }

    def unionFileLists(leftList: Option[String], rightList: Option[String]): Set[String] = {
        var allFiles = Set[String]()
        leftList.foreach { value => allFiles ++= value.split(",") }
        rightList.foreach { value => allFiles ++= value.split(",") }
        allFiles.filter { _.nonEmpty }
    }
}
