package iql.engine.auth

case class MLSQLTable(db: Option[String], table: Option[String], tableType: TableTypeMeta)

case class MLSQLTableSet(tables: Seq[MLSQLTable])

case class TableTypeMeta(name: String, includes: Set[String])

case class TableAuthResult(granted: Boolean, msg: String)

object TableAuthResult {
    def empty() = {
        TableAuthResult(false, "")
    }
}

object TableType {
    val HIVE = TableTypeMeta("hive", Set("hive"))
    val HBASE = TableTypeMeta("hbase", Set("hbase"))
    val HDFS = TableTypeMeta("hdfs", Set("parquet", "json", "csv", "orc", "text"))
    val KAFKA = TableTypeMeta("kafka", Set("kafka"))
    val JDBC = TableTypeMeta("jdbc", Set("jdbc"))
    val ES = TableTypeMeta("es", Set("es"))
    val TEMP = TableTypeMeta("temp", Set("temp","jsonStr"))

    def from(str: String) = {
        List(HIVE, HBASE, HDFS, KAFKA, JDBC, ES, TEMP).find(f => f.includes.contains(str))
    }

}