package iql.common.domain

import java.sql.Timestamp
import iql.common.utils.ObjGenerator

object Bean {

    case class IQLExcution(iql: String = "",
                           var mode: String = SQLMode.IQL,
                           startTime: Timestamp = new Timestamp(System.currentTimeMillis),
                           var takeTime: Long = 0,
                           var success: Boolean = true,
                           var hdfsPath: String = "",
                           var user: String = "",
                           var data: String = "",
                           var dataType: String = ResultDataType.STRUCTURED_DATA,
                           var schema: String = "",
                           var variables: String = "[]",
                           var status: String = JobStatus.RUNNING,
                           var engineInfoAndGroupId: String = ""){
        def toJSONObjet = {
            ObjGenerator.newJSON(Seq(("iql",iql),("mode",mode),("startTime",startTime),("takeTime",takeTime),
                ("isSuccess",success),("hdfsPath",hdfsPath),("user",user),("data",data),("dataType",dataType),
                ("schema",schema),("variables",variables),("status",status),("engineInfoAndGroupId",engineInfoAndGroupId)
            ):_*)
        }
    }

    case class IQLEngine(engineId: Int, engineInfo: String)

    case class Iql(mode: String, iql: String, variables: String)

    case class StopIQL()

    case class GetBatchResult(engineInfoAndGroupId: String)

    case class GetJobIdsForGroup(engineInfoAndGroupId: String)

    case class GetStreamStatus(streamName: String)

    case class GetActiveStream()

    case class StopSreamJob(streamName: String)

    case class StreamJobStatus(streamName: String)

    case class StreamJob(engineInfo: String, name: String, uid: String)

    case class CancelJob(groupId: Int)

    case class HiveCatalog()

    case class HiveCatalogWithAutoComplete()

    case class HbaseTables(zk:String)

    case class HiveTables()

}

object JobStatus {
    val RUNNING = "RUNNING"
    val FINISH = "FINISH"
}

object SQLMode {
    val IQL = "iql"
    val CODE = "code"
}

object ResultDataType {
    val ERROR_DATA = "errorData"
    val PRE_DATA = "preData"
    val STRUCTURED_DATA = "structuredData"
}

