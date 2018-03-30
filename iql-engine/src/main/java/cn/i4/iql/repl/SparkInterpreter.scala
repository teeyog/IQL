package cn.i4.iql.repl

import java.io.File
import java.net.URLClassLoader
import java.nio.file.{Files, Paths}

import org.apache.spark.{SparkConf, SparkUtils}
import org.apache.spark.repl.SparkILoop
import org.apache.spark.sql.SparkSession
import org.apache.spark.util.Utils

import scala.tools.nsc.{GenericRunnerSettings, Settings}
import scala.tools.nsc.interpreter.Completion.ScalaCompleter
import scala.tools.nsc.interpreter.{IMain, JLineCompletion, JPrintWriter}
import scala.tools.nsc.interpreter.Results.Result
import scala.util.control.NonFatal

class SparkInterpreter(protected override val spark: SparkSession) extends AbstractSparkInterpreter {

  private var sparkILoop: SparkILoop = _
  private var sparkHttpServer: Object = _


  def conf = spark.sparkContext.getConf

  private var hasErrors = false

  private def scalaOptionError(msg: String): Unit = {
    hasErrors = true
    Console.err.println(msg)
  }

  override def start(): Unit = {
    require(sparkILoop == null)

    val rootDir = conf.get("spark.repl.classdir", System.getProperty("java.io.tmpdir"))
    val outputDir = Files.createTempDirectory(Paths.get(rootDir), "spark").toFile
    outputDir.deleteOnExit()
    conf.set("spark.repl.class.outputDir", outputDir.getAbsolutePath)


    val settings = new Settings()
    settings.processArguments(List("-Yrepl-class-based",
      "-Yrepl-outdir", s"${outputDir.getAbsolutePath}"), true)
    settings.usejavacp.value = true
    settings.embeddedDefaults(Thread.currentThread().getContextClassLoader())

    sparkILoop = new SparkILoop(None, new JPrintWriter(outputStream, true))
    sparkILoop.settings = settings
    sparkILoop.createInterpreter()
    sparkILoop.initializeSynchronous()

    restoreContextClassLoader {
      sparkILoop.setContextClassLoader()

      var classLoader = Thread.currentThread().getContextClassLoader
      while (classLoader != null) {
        if (classLoader.getClass.getCanonicalName ==
          "org.apache.spark.util.MutableURLClassLoader") {
          val extraJarPath = classLoader.asInstanceOf[URLClassLoader].getURLs()
            // Check if the file exists. Otherwise an exception will be thrown.
            .filter { u => u.getProtocol == "file" && new File(u.getPath).isFile }
            // Livy rsc and repl are also in the extra jars list. Filter them out.
            .filterNot { u => Paths.get(u.toURI).getFileName.toString.startsWith("livy-") }
            // Some bad spark packages depend on the wrong version of scala-reflect. Blacklist it.
            .filterNot { u =>
            Paths.get(u.toURI).getFileName.toString.contains("org.scala-lang_scala-reflect")
          }

          extraJarPath.foreach { p => debug(s"Adding $p to Scala interpreter's class path...") }
          sparkILoop.addUrlsToClassPath(extraJarPath: _*)
          classLoader = null
        } else {
          classLoader = classLoader.getParent
        }
      }

      postStart()
    }
  }


  override def close(): Unit = synchronized {
    super.close()

    if (sparkILoop != null) {
      sparkILoop.closeInterpreter()
      sparkILoop = null
    }

    if (sparkHttpServer != null) {
      val method = sparkHttpServer.getClass.getMethod("stop")
      method.setAccessible(true)
      method.invoke(sparkHttpServer)
      sparkHttpServer = null
    }
  }

  override protected def isStarted(): Boolean = {
    sparkILoop != null
  }

  override protected def interpret(code: String): Result = {
    sparkILoop.interpret(code)
  }

  override protected def completeCandidates(code: String, cursor: Int) : Array[String] = {
    val completer : ScalaCompleter = {
      try {
        val cls = Class.forName("scala.tools.nsc.interpreter.PresentationCompilerCompleter")
        cls.getDeclaredConstructor(classOf[IMain]).newInstance(sparkILoop.intp)
          .asInstanceOf[ScalaCompleter]
      } catch {
        case e : ClassNotFoundException => new JLineCompletion(sparkILoop.intp).completer
      }
    }
    completer.complete(code, cursor).candidates.toArray
  }

  override protected def valueOfTerm(name: String): Option[Any] = {
    // IMain#valueOfTerm will always return None, so use other way instead.
    Option(sparkILoop.lastRequest.lineRep.call("$result"))
  }

  override protected def bind(name: String,
                              tpe: String,
                              value: Object,
                              modifier: List[String]): Unit = {
    sparkILoop.beQuietDuring {
      sparkILoop.bind(name, tpe, value, modifier)
    }
  }

}

