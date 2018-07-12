package cn.i4.iql

import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConverters._
import org.apache.spark.sql.streaming.StreamingQuery

class IQLSession(_engineInfo:String) {

  def engineInfo = _engineInfo

  // 保存batch任务engineInfo_iqlId和result的映射（获取某个SQL的结果或者kill，与对应engine通讯即可）
  val batchJob = new ConcurrentHashMap[String, String]()

  // 保存stream任务engineInfo和StreamingQuery的映射（查看某个stream的状态或者stop某个stream）
  val streamJob = new ConcurrentHashMap[String, StreamingQuery]().asScala

}

object IQLSeassion{

}