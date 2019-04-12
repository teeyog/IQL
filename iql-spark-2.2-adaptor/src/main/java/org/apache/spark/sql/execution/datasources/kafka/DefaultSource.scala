package org.apache.spark.sql.execution.datasources.kafka

import com.twitter.bijection.Injection
import com.twitter.bijection.avro.GenericAvroCodecs
import kafka.serializer.{DefaultDecoder, StringDecoder}
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.sources.{BaseRelation, DataSourceRegister, RelationProvider, TableScan}
import org.apache.spark.sql.types.{StringType, StructField, StructType}
import org.apache.spark.sql.{Row, SQLContext}
import org.apache.spark.streaming.kafka.{KafkaUtils, OffsetRange}
import org.apache.spark.unsafe.types.UTF8String

class DefaultSource extends RelationProvider with DataSourceRegister {
    override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
        val fieldName = parameters.getOrElse("fieldName", "msg")
        new KafkaRelation(fieldName, parameters)(sqlContext)
    }

    override def shortName(): String = "kafka"
}

private[sql] class KafkaRelation(val fieldName: String,
                                 val kafkaParams: Map[String, String]
                                )(@transient val sqlContext: SQLContext)
  extends BaseRelation with TableScan with Serializable {


    val I4_SCHEMA: String =
        """
          |{
          |    "fields": [
          |        { "name": "data", "type": "string" }
          |    ],
          |    "name": "I4",
          |    "type": "record"
          |}
        """.stripMargin
    lazy val schema_kafka: Schema = new Schema.Parser().parse(I4_SCHEMA)
    lazy val recordInjection: Injection[GenericRecord, Array[Byte]] = GenericAvroCodecs.toBinary(schema_kafka)

    override def schema: StructType = StructType(Seq(StructField("key", StringType, false),StructField(fieldName, StringType, false)))

    override def buildScan(): RDD[Row] = {
        val sc = sqlContext.sparkContext
        val kc = KafkaClusterExtension(kafkaParams)
        val groupId = kafkaParams.getOrElse("group.id", sys.error("Consume kafka must give the property groupId ..."))
        val topicSet: Set[String] = kafkaParams.getOrElse("topics", sys.error("Consume kafka must give the property topics ...")).split(",").toSet
        val maxRatePerPartition = kafkaParams.getOrElse("maxRatePerPartition","100000")
        val autoCommitOffset:Boolean = kafkaParams.getOrElse("autoCommitOffset", "false").toBoolean
        val offsetRange = kc.getConsumerOffsetByZKAndKafka_(groupId, topicSet).map { case (tp, s) =>
            val endOffset = Math.min(s._1 + maxRatePerPartition.toInt,s._2)
            OffsetRange(tp, s._1, endOffset)
        }.toArray


        val rdd: RDD[Row] =
            kafkaParams.getOrElse("record.serializable.type", "default").toString.toLowerCase match {
                case "avro" =>
                    KafkaUtils.createRDD[String, String, StringDecoder, AvroDecoder](
                        sc,
                        kafkaParams,
                        offsetRange
                    ).map { line =>
                        Row.fromSeq(Seq(if(null == line._1) "" else line._1,line._2))
                    }
                case _ =>
                    KafkaUtils.createRDD[String, String, StringDecoder, StringDecoder](
                        sc,
                        kafkaParams,
                        offsetRange
                    ).map { line =>
                        Row.fromSeq(Seq(if(null == line._1) "" else line._1,line._2))
                    }
            }
        if(autoCommitOffset){
            kc.saveConsumerOffset(topicSet.head, groupId, offsetRange)
        }
        rdd
    }
}