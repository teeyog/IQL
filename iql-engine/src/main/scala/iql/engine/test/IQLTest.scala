package iql.engine.test

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.TableIdentifier
import org.apache.spark.sql.catalyst.analysis.UnresolvedRelation
import org.apache.spark.sql.catalyst.catalog.HiveTableRelation
import org.apache.spark.sql.catalyst.plans.logical.{Aggregate, Project}
import org.apache.spark.sql.execution.datasources.LogicalRelation

object IQLTest {

  def main(args: Array[String]): Unit = {


    val spark = SparkSession
        .builder
        .appName("IQL")
        .master("local[4]")
        .enableHiveSupport()
        .getOrCreate()
    spark.sparkContext.setLogLevel("WARN")


     spark.sql(
      """
select idfa,uuid from mc.mbl where date='20180912' limit 10
        """.stripMargin).createOrReplaceTempView("ttt")

//    val df = spark.sql(
//      """
//          drop table mc.test
//        """.stripMargin)

    val plan = spark.sessionState.sqlParser.parsePlan(
      """
drop table mc.test
        """.stripMargin)

    println(plan)
    plan

    .transformUp{
      case i@UnresolvedRelation(tableIdentifier) =>
        println(tableIdentifier.table + " -- " + tableIdentifier.database.getOrElse("None"))
        i
    }

//    df.queryExecution.logical
//    df.queryExecution.analyzed
//        .foreach {
//      case Project(projectList, child) =>
//        attributeSet.append(projectList.map(_.references): _*)
//      case aggregate: Aggregate =>
//        attributeSet.append(aggregate.aggregateExpressions.map(_.references): _*)
//        attributeSet.append(aggregate.groupingExpressions.map(_.references): _*)

//      case HiveTableRelation(tableMeta,dataCols,partitionCols) =>
//        dataCols.foreach(a => println( "--" +  a.name))
//      case LogicalRelation(relation,output,catalogTable, _) =>
//        if(catalogTable.nonEmpty){
//          val identifier = catalogTable.get.identifier
//          println(identifier.database.get + " -- " + identifier.table)
//        }else{
//          relation.schema.fields.map(s => println(s.name))
//        }
//      case UnresolvedRelation(tableIdentifier) =>  println(tableIdentifier.table + " -- " + tableIdentifier.database.getOrElse("None"))

//      case other =>
//    }

//    println(df.queryExecution.analyzed)




    // Subscribe to 1 topic
    //    val df = spark
    //      .readStream
    //      .option("kafka.bootstrap.servers", "dsj02:9092,dsj03:9092,dsj04:9092,dsj04:9092")
    //      .option("subscribe", "i4-monitor")
    //      .option("startingoffsets", "latest")
    //      .option("failOnDataLoss", "false")
    //      .format("kafka")
    //      .load()
    //    spark.sparkContext.setLogLevel("WARN")
    //
    //    val wordCounts = df.selectExpr("CAST(value AS STRING)")
    //      .as[String].groupBy("value").count()
    //
    //    val query = wordCounts.writeStream.foreach(new ForeachWriter[Row] {
    //      override def process(value: Row): Unit = println(value.get(0))
    //
    //      override def close(errorOrNull: Throwable): Unit = {}
    //
    //      override def open(partitionId: Long, version: Long): Boolean = true
    //    })
    //      .outputMode("complete")
    //      .option("checkpointLocation", "/tmp/cp/cp81")
    //      .format("console")
    //      .trigger(Trigger.ProcessingTime(5, TimeUnit.SECONDS))
    //      .start()
    //    query.awaitTermination()


  }
}

