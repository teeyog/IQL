package cn.i4.iql.domain

object Bean {

  case class IQLEngine(engineId:Int,engineInfo:String,var name:String = "") {
    def name(name:String):Unit = {
      this.name = name
    }
  }

  case class Iql(code:String,iql:String, variables:String)

  case class StopIQL()

  case class GetResult(engineInfoAndGroupId:String)

  case class CancelJob(groupId:Int)

  case class HiveCatalog()

  case class HiveCatalogWithAutoComplete()
}
