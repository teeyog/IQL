package iql.engine.auth

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.catalyst.catalog.{CatalogTable, HiveTableRelation}
import org.apache.spark.sql.catalyst.expressions.{AttributeReference, AttributeSet, Exists, Expression, ListQuery, NamedExpression, ScalarSubquery}
import org.apache.spark.sql.catalyst.plans.logical._
import org.apache.spark.sql.execution.datasources.LogicalRelation
import org.apache.spark.sql.sources.BaseRelation

import scala.collection.mutable.ArrayBuffer

object CheckAuth {

    val checkRule = (spark: SparkSession) => {
        (plan: LogicalPlan) => {
            //            println(plan)
            val physicalColumns = new ArrayBuffer[AttributeSet]()
            val availableColumns = collectRelation(plan).flatMap { relation =>
                val tableMate = if (relation.isInstanceOf[LogicalRelation]) {
                    relation.asInstanceOf[LogicalRelation].catalogTable
                } else {
                    Some(relation.asInstanceOf[HiveTableRelation].tableMeta)
                }
                tableMate match {
                    case Some(table) =>
                        table.identifier.database
                        //                        val catalogTable = mbSession.getCatalogTable(table.identifier.table, table.identifier.database)//tableIdentifierToCatalogTable.get(table.identifier)
                        physicalColumns.append(relation.references)
                        //                        if (catalogTable.createBy == catalogSession.userId) {
                        relation.references
                    //                        } else {
                    //                            val visibleColumns = new TablePrivilegeManager(mbSession, catalogTable).selectable().map(_.name)
                    //                            relation.references.filter(attr => visibleColumns.contains(attr.name))
                    //                        }
                    case None =>
                        Seq()
                }
            }
            val attributeSet = new ArrayBuffer[AttributeSet]()

            plan.foreach {
                case Project(projectList, child) =>
                    attributeSet.append(projectList.map(_.references): _*)
                case aggregate: Aggregate =>
                    attributeSet.append(aggregate.aggregateExpressions.map(_.references): _*)
                    attributeSet.append(aggregate.groupingExpressions.map(_.references): _*)

                case HiveTableRelation(tableMeta,dataCols,partitionCols) =>
                    dataCols.foreach(a => println( "--" +  a.name))
                case LogicalRelation(_,output,catalogTable, _) =>
                    output.foreach(a => println("**" +  a.name))
                    catalogTable.get.viewQueryColumnNames.foreach(println)
                case other =>
            }

//            if (attributeSet.nonEmpty) {
//                val unavailableColumns: AttributeSet = //if (physicalColumns.isEmpty) { // for `select literal`
//                    attributeSet.reduce(_ ++ _) -- availableColumns
//                //                } else {
//                //                    attributeSet.reduce(_ ++ _).filter(physicalColumns.reduce(_ ++ _).contains) -- availableColumns
//                //                }
//                println(s"""availableColumns column ${availableColumns.map(attr => s"'${attr.name}'").mkString(", ")}""".stripMargin)
//                //                println(s"""attributeSet column ${attributeSet.foreach(attr => attr.map(a => s"'${a.name}'").mkString(", ")) }""".stripMargin)
//                attributeSet.foreach(set => println(s"""availableColumns column ${set.map(attr => s"'${attr.name}'").mkString(", ")}""".stripMargin))
//                if (unavailableColumns.nonEmpty) {
//                    throw new Exception(
//                        s""" SELECT command denied to user UFO for column ${unavailableColumns.map(attr => s"'${attr.name}'").mkString(", ")}""".stripMargin)
//                } else {
//                    println(s"""SELECT command denied to user UFO for column ${unavailableColumns.map(attr => s"'${attr.name}'").mkString(", ")}""".stripMargin)
//                }
//            }


        }
    }

    private def collectRelation(plan: LogicalPlan): Seq[LogicalPlan] = {

        def traverseExpression(expr: Expression): Seq[LogicalPlan] = {
            expr.flatMap {
                case ScalarSubquery(child, _, _) => collectRelation(child)
                case Exists(child, _, _) => collectRelation(child)
                case ListQuery(child, _, _, _) => collectRelation(child)
                case a => a.children.flatMap(traverseExpression)
            }
        }

        plan.collect {
            case l: LogicalRelation => Seq(l)
            case c: HiveTableRelation => Seq(c)
            case project: Project =>
                project.projectList.flatMap(traverseExpression)
            case aggregate: Aggregate =>
                aggregate.aggregateExpressions.flatMap(traverseExpression)
            case With(_, cteRelations) =>
                cteRelations.flatMap {
                    case (_, SubqueryAlias(_, child)) => collectRelation(child)
                }
            case Filter(condition, _) =>
                traverseExpression(condition)
        }.flatten
    }

}
