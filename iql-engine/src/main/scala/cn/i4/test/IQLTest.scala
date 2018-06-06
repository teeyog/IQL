package cn.i4.test

import java.util.concurrent.ConcurrentHashMap

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import cn.i4.iql.{ExeActor, IQLSQLExecListener}
import org.apache.spark.sql.SparkSession

object IQLTest {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder
        .appName("iql")
        .master("local[4]")
      .enableHiveSupport()
      .getOrCreate()

    spark.sql("select * from tb").show()
//
//    val actorSystem = ActorSystem("iqlSystem")
//    val actor1 = actorSystem.actorOf(ExeActor.props(spark), name = "engine")
//
////    val actor2 = actorSystem.actorOf(Props(new TestActor(spark)), name = "actor2")
//val input3 =
//"""
//  connect jdbc where driver="com.mysql.jdbc.Driver"
// |    and url="jdbc:mysql://192.168.1.233:3306/logweb-pro?user=root&password=123456&useUnicode=true&characterEncoding=UTF8&useSSL=false"
// |    and user="root"
// |    and password="123456"
// |    as i4;
// |
// |load jdbc.`i4.app` as app;
// |select * from app limit 100 as tb1;
// |save tb1 as json.`/tmp/iql/todd1.json`
//""".stripMargin
//    actor1 ! input3





  }

}
