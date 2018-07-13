package cn.i4.iql.domain

object Bean {

  case class IQLEngine(engineId:Int,engineInfo:String)

  case class Iql(code:String,iql:String, variables:String)

  case class StopIQL()

  case class GetBatchResult(engineInfoAndGroupId:String)

  case class GetStreamStatus(streamName:String)

  case class GetActiveStream()

  case class StopSreamJob(streamName:String)

  case class StreamJobStatus(streamName:String)

  case class StreamJob(engineInfo:String,name:String,uid:String)

  case class CancelJob(groupId:Int)

  case class HiveCatalog()

  case class HiveCatalogWithAutoComplete()
}
