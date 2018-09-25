package iql.engine.auth

import com.alibaba.fastjson.JSONArray
import iql.common.utils.{HttpUtils, ObjGenerator}
import iql.engine.utils.PropsUtils

object DataAuth {

    def auth(table: List[MLSQLTable]): List[TableAuthResult] ={
        table.filter(_.tableType.name != "temp").foreach(t => println(t.tableType.name + " -- " + t.db.getOrElse("") + " -- " + t.table.getOrElse("")))

        val tableArray = new JSONArray()
        table.filter(_.tableType.name != "temp").foreach(t => {
            tableArray.add(ObjGenerator.newJSON(Seq(("type",t.tableType.name),("db",t.db.getOrElse("")),("table",t.table.getOrElse(""))):_*))
        })

        val url = PropsUtils.get("iql.server.address") + "/auth/checkAuth"
        val result = HttpUtils.get(url,ObjGenerator.newMap(Seq(("token", "fa39e32c09332d47f6f38d9c946cfa25"),("data",tableArray.toJSONString)):_*),5,"utf-8")
        println(result)
        null
    }

}
