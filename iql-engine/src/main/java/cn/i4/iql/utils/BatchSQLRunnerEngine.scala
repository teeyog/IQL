package cn.i4.iql.utils

object BatchSQLRunnerEngine {

  /**
    * 全局唯一ID
    */
  var groupId:Int = 0

  def getGroupId:Int = {
    this.synchronized {
      groupId += 1
      groupId
    }
  }

}
