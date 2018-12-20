package iql.engine

import java.util.concurrent.locks.ReentrantLock
import java.util.concurrent.{ConcurrentHashMap, TimeUnit}

import iql.common.domain.Bean.IQLExcution
import org.apache.spark.sql.streaming.StreamingQuery

import scala.collection.JavaConverters._

class IQLSession(_engineInfo:String) {

  def engineInfo = _engineInfo

  // 保存batch任务engineInfo_iqlId和result的映射（获取某个SQL的结果或者kill，与对应engine通讯即可）
  val batchJob = new ConcurrentHashMap[String, IQLExcution]()

  // 保存stream任务engineInfo和StreamingQuery的映射（查看某个stream的状态或者stop某个stream）
  val streamJob = new ConcurrentHashMap[String, StreamingQuery]().asScala

  // 实时任务对应的邮件receiver
  val streamJobWithMailReceiver = new ConcurrentHashMap[String, String]()
  var streamJobWithDingDingReceiver:Set[String] = Set()

  private val lock = new ReentrantLock()
  private val condition = lock.newCondition()
  private var stopped: Boolean = false
  private var error: Throwable = null

  def awaitTermination(timeout: Long = -1): Boolean = {
    lock.lock()
    try {
      if (timeout < 0) {
        while (!stopped && error == null) {
          condition.await()
        }
      } else {
        var nanos = TimeUnit.MILLISECONDS.toNanos(timeout)
        while (!stopped && error == null && nanos > 0) {
          nanos = condition.awaitNanos(nanos)
        }
      }
      // If already had error, then throw it
      if (error != null) throw error
      // already stopped or timeout
      stopped
    } finally {
      lock.unlock()
    }
  }

}

object IQLSeassion{

}