package cn.i4.iql.domain

object Bean {

  case class IQLEngine(engineId:Int,engineInfo:String)

  case class Iql(code:String,iql:String, variables:String)

  case class StopIQL()

  case class GetResult(engineInfoAndGroupId:String)

  case class CancelJob(groupId:Int)

  case class HiveCatalog()

  case class HiveCatalogWithAutoComplete()
}
