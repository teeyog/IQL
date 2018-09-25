package iql.engine.auth

import com.alibaba.fastjson.{JSON, JSONArray}
import iql.common.utils.{HttpUtils, ObjGenerator}
import iql.engine.utils.PropsUtils

object DataAuth {

    def auth(table: List[MLSQLTable]): List[TableAuthResult] ={
        //构造jsonArray
        val tableArray = new JSONArray()
        table.filter(_.tableType.name != "temp").foreach(t => {
            tableArray.add(ObjGenerator.newJSON(Seq(("type",t.tableType.name),("db",t.db.getOrElse("")),("table",t.table.getOrElse(""))):_*))
        })
        //请求
        val url = PropsUtils.get("iql.server.address") + "/auth/checkAuth"
        val result = HttpUtils.get(url,ObjGenerator.newMap(Seq(("token", PropsUtils.get("token")),("data",tableArray.toJSONString)):_*),5,"utf-8")
        //结果映射
        var tar:List[TableAuthResult] = List[TableAuthResult]()
        val iter = JSON.parseArray(result).iterator()
        while (iter.hasNext){
            val obj = JSON.parseObject(iter.next().toString)
            tar = List(TableAuthResult(obj.getBooleanValue("granted"),obj.getString("msg"))) ::: tar
        }
        tar
    }

}
