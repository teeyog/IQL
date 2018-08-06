//package org.apache.spark.repl;
//
//
//import java.io.File;
//import java.io.PrintWriter;
//import java.lang.reflect.Field;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Map;
//
//import org.apache.spark.SparkConf;
//import org.apache.spark.SparkContext;
//import org.apache.spark.repl.SparkILoop;
//import org.apache.spark.sql.SparkSession;
//
//import scala.Console;
//import scala.collection.JavaConversions;
//import scala.tools.nsc.Settings;
//import scala.tools.nsc.interpreter.IMain;
//import scala.tools.nsc.interpreter.Results;
//
//public class ReplTest {
//    SparkSession session;
//    SparkConf conf;
//    SparkContext context;
//    SparkILoop sparkLoop;
//    Object intp;
//
//    public ReplTest(String masterURL) {
//
//
//        sparkLoop = new SparkILoop((java.io.BufferedReader) null, new PrintWriter(Console.out(), false));
//
//        Settings settings = new Settings();
//
//        LinkedList<String> argList = new LinkedList<>();
//        argList.add("-usejavacp");
//        argList.add("-Yrepl-class-based");
//        argList.add("-Yrepl-outdir");
//        argList.add("/Users/rernas/test/ff");
//        argList.add("-classpath");
//
//        String classpath = System.getProperty("java.class.path");
//        argList.add(classpath);
//
//        scala.collection.immutable.List<String> list = JavaConversions.asScalaBuffer(argList).toList();
//
//        settings.processArguments(list, true);
//        sparkLoop.settings_$eq(settings);
//        sparkLoop.createInterpreter();
//        intp = Utils.invokeMethod(sparkLoop, "intp");
//
//
//        Utils.invokeMethod(intp, "setContextClassLoader");
//        Utils.invokeMethod(intp, "initializeSynchronous");
//
//        System.out.println("settings.outputDirs().getSingleOutput().get() : " + settings.outputDirs().getSingleOutput().get());
//
////      Results.Result res = interpret("import java.lang.Integer");
////      res = interpret("val i : Integer = new Integer(2)");
////      res = interpret("val j : Integer = new Integer(3)");
////      res = interpret("val r = i + j");
//
//
//        conf = new SparkConf().setMaster(masterURL);
//        conf.set("spark.repl.class.outputDir", "/Users/rernas/test/ff");
//        conf.set("spark.scheduler.mode", "FAIR");
//        Class SparkSession = Utils.findClass("org.apache.spark.sql.SparkSession");
//        Object builder = Utils.invokeStaticMethod(SparkSession, "builder");
//        Utils.invokeMethod(builder, "config", new Class[] { SparkConf.class }, new Object[] { conf });
//
//        session = (SparkSession) Utils.invokeMethod(builder, "getOrCreate");
//
//        context = (SparkContext) Utils.invokeMethod(session, "sparkContext");
//
//
//
//        importCommonSparkPackages();
//
//        bindSparkComponents();
//
//        System.out.println("intp:" + intp);
//        System.out.println("session : " + session);
//        System.out.println("context : " + context);
//
//    }
//
//    private void bindSparkComponents() {
//        interpret("@transient val _binder = new java.util.HashMap[String, Object]()");
//        Map<String, Object> binder = (Map<String, Object>) getLastObject();
//        binder.put("sc", context);
//        binder.put("conf", conf);
//
//        interpret("@transient val sc = "
//                + "_binder.get(\"sc\").asInstanceOf[org.apache.spark.SparkContext]");
//        interpret("@transient val conf = "
//                + "_binder.get(\"conf\").asInstanceOf[org.apache.spark.SparkConf]");
//
//    }
//
//    private void importCommonSparkPackages() {
//        Results.Result res = interpret("import org.apache.spark._");
//        res = interpret("import org.apache.spark.streaming._");
//        res = interpret("import org.apache.spark.streaming.StreamingContext._ ");
//        res = interpret("import org.apache.spark._");// replace
//    }
//
//    public Object getLastObject() {
//        IMain.Request r = (IMain.Request) Utils.invokeMethod(intp, "lastRequest");
//        if (r == null || r.lineRep() == null) {
//            return null;
//        }
//        Object obj = r.lineRep().call("$result",
//                JavaConversions.asScalaBuffer(new LinkedList<>()));
//        return obj;
//    }
//
//    private Results.Result interpret(String line){
//        return (Results.Result) Utils.invokeMethod(intp, "interpret", new Class[] { String.class },
//                new Object[] { line });}
//
//    private File createTempDir(String dir) {
//        File file = null;
//
//        // try Utils.createTempDir()
//        file = (File) Utils.invokeStaticMethod(
//                Utils.findClass("org.apache.spark.util.Utils"),
//                "createTempDir",
//                new Class[]{String.class, String.class},
//                new Object[]{dir, "spark"});
//
//        // fallback to old method
//        if (file == null) {
//            file = (File) Utils.invokeStaticMethod(
//                    Utils.findClass("org.apache.spark.util.Utils"),
//                    "createTempDir",
//                    new Class[]{String.class},
//                    new Object[]{dir});
//        }
//
//        return file;
//    }
//
//    public void execute(String... lines) {
//        for (String line : lines ){
//            interpret(line);
//        }
//    }
//
//    public static void main(String[] args) {
//        ReplTest v = new ReplTest("spark://ilkers-MacBook-Pro.local:7077");
////      VerapiInterpreter v = new VerapiInterpreter("local");
//        v.execute(
//                "import org.apache.spark.streaming.Seconds",
//                "var ssc = new StreamingContext(sc,Seconds(10)) ",
//                "val dstream = ssc.socketTextStream(\"localhost\", 9000)",
////                  "dstream.foreachRDD(rdd => rdd.saveAsObjectFile(\"/Users/rernas/test/ff\"))",
//                "dstream.foreachRDD(rdd => rdd.foreach(x => println(x)))",
////                  "val emptyRDD = sc.parallelize(Seq(\"bisi\"))",
////                  "emptyRDD.foreach(x => println(x))"
//                "ssc.start()",
//                "ssc.awaitTermination()"
//        );
//    }
//
//}