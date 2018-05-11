package cn.i4.iql.domain

object Bean {

  case class IQLEngine(engineId:Int,engineInfo:String,var name:String = "") {
    def name(name:String):Unit = {
      this.name = name
    }
  }

  case class ShakeHands()

  case class Iql(code:String,iql:String,engineId:Int)

  case class StopIQL()

  case class Databases()

  case class Tables(database:String)

  case class GetResult(engineIdAndGroupId:String)

  case class CancelJob(groupId:Int)

  case class HiveCatalog()
}
