package cn.i4.iql.repl

import java.io.File

import org.apache.spark.repl.SparkILoop
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions._
import org.apache.spark.{SparkConf, SparkUtils}

object UdfTest {
    val spark = SparkSession.builder()
            .appName("udftest")
//            .master("local")
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
//        notInterpreted()
        interpret(withoutUdfString)/**/
//        val s =
//            """
//              | spark.sparkContext.parallelize(Seq(("A",12),("B",13))).reduceByKey(_+_).foreach(println)
//            """.stripMargin
//        SparkILoop.run(s)
//        interpret(s)
//        interpret(withUdfString)
//        spark.stop()

//        spark.sparkContext.parallelize(Seq(("A",12),("B",13)))
//            .reduceByKey(_+_).foreach(println)
    }

    def interpret(script:String) {
        import scala.tools.nsc.GenericRunnerSettings
        import scala.tools.nsc.interpreter.IMain

        val conf = new SparkConf()
        val rootDir = conf.getOption("spark.repl.classdir").getOrElse(SparkUtils.getLocalDir(conf))
        val outputDir = SparkUtils.createTempDir(root = rootDir, namePrefix = "repl")
        conf.set("spark.repl.class.outputDir", outputDir.getAbsolutePath())

        outputDir.deleteOnExit()
        conf.set("spark.repl.class.outputDir", outputDir.getAbsolutePath)

        val cl = ClassLoader.getSystemClassLoader

        val settings = new GenericRunnerSettings( println _ )
        val jars = (SparkUtils.getUserJars(conf) ++ cl.asInstanceOf[java.net.URLClassLoader].getURLs.map(_.toString)).mkString(File.pathSeparator)
        val interpArguments = List(
            "-usejavacp",
            "-Yrepl-class-based",
            "-Yrepl-outdir",
            s"${outputDir.getAbsolutePath}",
            "-classpath",
            jars
        )

        settings.processArguments(interpArguments, true)
        settings.usejavacp.value = true

        val intp = new IMain(settings, new java.io.PrintWriter(System.out))
        intp.setContextClassLoader
        intp.initializeSynchronous

        intp.bind("spark", spark)
        intp.interpret(script)
    }


}
