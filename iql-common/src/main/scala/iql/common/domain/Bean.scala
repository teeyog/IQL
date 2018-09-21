package iql.common.domain

import java.sql.Timestamp

object Bean {

  case class IQLExcution(iql: String, mode: String, startTime: Timestamp, takeTime: Long, isSuccess: Boolean, resultPath: String, description: String, errorMessage: String, content: String, tableSchema: String, variables: String)

  case class IQLEngine(engineId:Int,engineInfo:String)

  case class Iql(mode:String,iql:String, variables:String)

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
