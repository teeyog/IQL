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
//
//    spark.sql("select * from i4.mbl where m_date='20180607' limit 10").rdd.map(r => (r.get(0),r.get(1))).reduceByKey(_+_)
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
//声明一个类

//    //声明一个样本类
//    case class MyCaseClass(number: Int, text: String, others: List[Int]){
//      println(number)
//    }
//    //不需要new关键字，创建一个对象
//    val dto = MyCaseClass(3, "text", List.empty) //打印结果3
//
//    //利用样本类默认实现的copy方法
//    dto.copy(number = 5) //打印结果5
//
//    val dto2 = MyCaseClass(3, "text", List.empty)
//    println(dto == dto2) // 返回true，两个不同的引用对象
//    class MyClass(number: Int, text: String, others: List[Int]) {}
//    val c1 = new MyClass(1, "txt", List.empty)
//    val c2 = new MyClass(1, "txt", List.empty)
//    println(c1 == c2 )// 返回false,两个不同的引用对象
//
//
//     List[Nothing]


 println((0 to 10).sum)

  }

}

