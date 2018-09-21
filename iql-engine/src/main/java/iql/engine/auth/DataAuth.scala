package iql.engine.auth

object DataAuth {

    def auth(table: List[MLSQLTable]): List[TableAuthResult] ={
        table.filter(_.tableType.name != "temp").foreach(t => println(t.tableType.name + " -- " + t.db.getOrElse("") + " -- " + t.table.getOrElse("")))
        null
    }

}
