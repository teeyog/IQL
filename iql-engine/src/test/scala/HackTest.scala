import java.util
import java.util.concurrent.TimeUnit
import javax.ws.rs.client.{Client, ClientBuilder}
import javax.ws.rs.core.MediaType

import iql.common.utils.HttpUtils
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.streaming.Trigger
import org.glassfish.jersey.client.ClientProperties

object HackTest {

    def main(args: Array[String]): Unit = {
        val spark = SparkSession.builder()
            .appName("测试")
            .master("local[4]")
            .enableHiveSupport()
            .getOrCreate()

        val df = spark
            .readStream
            .option("kafka.bootstrap.servers", "192.168.1.61:9092")
            .option("subscribe", "mc-alermclock")
            .option("startingoffsets", "latest")
            .option("failOnDataLoss", "false")
            .format("kafka")
            .load()

        val query = df.writeStream
            .outputMode("append")
            .option("checkpointLocation", "/tmp/cp/cp2")
            .trigger(Trigger.ProcessingTime(10, TimeUnit.SECONDS))
            .format("console")
            .start()

        query.awaitTermination()

//
//        val JSON_REGEX = "abc(.*)".r
//import spark.implicits._
//        val table = spark.createDataset(Seq("""abc{"name":"UFO","age":13}"""))
//        spark.read.option("timestampFormat", "yyyy/MM/dd HH:mm:ss ZZ").json(
//                        table.rdd.map(r => {
//                          r match {
//                            case JSON_REGEX(jsonStr) => jsonStr
//                          }
//                        })
//        ).show(false)
    }

    //        val array = spark.sql("select * from table").rdd.randomSplit(Array())

    //        val rdd = spark.range(5).rdd
    //        rdd.map(r => rdd)
    //          .foreach(println)
    //        rdd.map(r => r).foreach(println)


    //
    //import spark.implicits._
    //        val df = spark.createDataset(Seq((1,"ufo1", "play", 15), (2,"yy", "", 34), (2,"yy3", "3", 343))).toDF("id","name", "like", "age")
    //        df
    //            .write.mode(SaveMode.Update).jdbc(Context.mysql_url,"aaa_test",new Properties())

    //         /** ************************************************************************
    //          * ********************************* 读 HBase******************************
    //          * ***********************************************************************/
    //
    //        // spark.table.schema,hbase.table.schema: spark和hbase对应的schema, 若指定后，只会扫描对应的列；
    //        // 若不指定，则会生成rowkey,content两个字段的dataframe，后者包括了所有字段形成的json。(建议指定)
    //
    //        /**
    //          * 方式一
    //          */
    //        val df = spark.
    //            read.format("org.apache.spark.sql.execution.datasources.hbase"). //驱动程序，类似JDBC的 driver class
    //            options(Map(
    //            "spark.table.schema" -> "ID:String,date:String,appid:String,appname:String,count:String",
    //            "hbase.table.schema" -> ":rowkey,0:DATE,0:APPID,0:APPNAME,0:COUNT",
    //            "hbase.zookeeper.quorum" -> "dsj01:2181",
    //            "inputTableName" -> "AI_IP_TEST"
    //        )).load
    //        df.show(false)
    //
    //        /**
    //          * 方式二
    //          */
    //
    //        import org.apache.hack.spark._
    //
    //        val options = Map(
    //            "spark.table.schema" -> "appid:String,appstoreid:int,apptype:String,firm:String",
    //            "hbase.table.schema" -> ":rowkey,info:appStoreId,info:appType,info:firm"
    //        )
    //        spark.hbaseTableAsDataFrame("test_delete1", Some("dsj01:2181")).show(false)
    //
    //
    //        /** ************************************************************************
    //          * ********************************* 写 HBase******************************
    //          * ***********************************************************************/
    //
    //        import spark.implicits._
    //
    //        val df1 = spark.createDataset(Seq((1,"1","1",null),(2,"2332","1","1"))).toDF("userid","like","osversion","toolversion")
    ////
    ////        // rowkey.filed: rowkey对应的字段，默认使用dataframe第一个字段
    ////        // outputTableName: hbase表名
    ////        // hbase.zookeeper.quorum: zk地址，和'zk'等价
    ////        // startKey,endKey,numReg:当表不存在时会创建表，三个字段要嘛都指定，要嘛都不指定，默认是一个region
    ////        // bulkload.enable 默认使用bulkload, 当存在只有rowkey的情况下，只能使用bulkload
    ////
    ////        /**
    ////          * 方式一
    ////          */
    //      df1.show(false)
    //      df1.printSchema()
    //        df1.write.format("org.apache.spark.sql.execution.datasources.hbase")
    //            .options(Map(
    //                "rowkey.filed" -> "name",
    //                "outputTableName" -> "test_delete3",
    //                "hbase.zookeeper.quorum" -> "dsj01:2181",
    //                "startKey" -> "00000000000000000000000000000000",
    //                "endKey" -> "ffffffffffffffffffffffffffffffff",
    //                "numReg" -> "12"
    //            )).save()
    //
    //
    //        /**
    //          * 方式二
    //          */
    //        val options1 = Map(
    //            "startKey" -> "00000000000000000000000000000000",
    //            "endKey" -> "ffffffffffffffffffffffffffffffff",
    //            "numReg" -> "12"
    //        )
    //        df1.saveToHbase("test_delete4", Some("dsj01:2181"), options1)


}
