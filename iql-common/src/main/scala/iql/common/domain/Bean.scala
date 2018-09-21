package iql.common.domain

import java.sql.Timestamp

import com.alibaba.fastjson.{JSON, JSONObject}


object Bean {

    case class IQLExcution(iql: String = "",
                           var mode: String = SQLMode.IQL,
                           startTime: Timestamp = new Timestamp(System.currentTimeMillis),
                           var takeTime: Long = 0,
                           var success: Boolean = false,
                           var hdfsPath: String = "",
                           var user: String = "",
                           var errorMessage: String = "",
                           var content: String = "",
                           var schema: String = "",
                           var variables: String = "[]",
                           var status: String = JobStatus.RUNNING,
                           var engineInfoAndGroupId: String = ""){
        def toJSONObjet = {
            val obj = new JSONObject()
            obj.put("iql",iql)
            obj.put("mode",mode)
            obj.put("startTime",startTime)
            obj.put("takeTime",takeTime)
            obj.put("isSuccess",success)
            obj.put("hdfsPath",hdfsPath)
            obj.put("user",user)
            obj.put("errorMessage",errorMessage)
            obj.put("content",content)
            obj.put("schema",schema)
            obj.put("variables",variables)
            obj.put("status",status)
            obj.put("engineInfoAndGroupId",engineInfoAndGroupId)
            obj
        }
    }

    case class IQLEngine(engineId: Int, engineInfo: String)

    case class Iql(mode: String, iql: String, variables: String)

    case class StopIQL()

    case class GetBatchResult(engineInfoAndGroupId: String)

    case class GetStreamStatus(streamName: String)

    case class GetActiveStream()

    case class StopSreamJob(streamName: String)

    case class StreamJobStatus(streamName: String)

    case class StreamJob(engineInfo: String, name: String, uid: String)

    case class CancelJob(groupId: Int)

    case class HiveCatalog()

    case class HiveCatalogWithAutoComplete()

}

object JobStatus {
    val RUNNING = "RUNNING"
    val FINISH = "FINISH"
}

object SQLMode {
    val IQL = "iql"
    val CODE = "code"
}

