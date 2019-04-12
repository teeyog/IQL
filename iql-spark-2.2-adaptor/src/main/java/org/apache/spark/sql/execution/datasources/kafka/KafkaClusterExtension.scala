package org.apache.spark.sql.execution.datasources.kafka

import java.util.Properties

import kafka.common.TopicAndPartition
import org.apache.spark.SparkException
import org.apache.spark.streaming.kafka.{KafkaCluster, OffsetRange}
import scala.collection.JavaConverters.propertiesAsScalaMapConverter

/**
  * @param kafkaParams
  */
class KafkaClusterExtension(kafkaParams: Map[String, String]) extends Serializable {

    /**
      *
      * @param kafkaConsumerConfig kafkaConsumer config
      */
    def this(kafkaConsumerConfig: Properties) = {
        this(kafkaConsumerConfig.asScala.toMap)
    }

    lazy val kc = new KafkaCluster(kafkaParams)

    /**
      * wrapper version using in sparkStreaming
      *
      * @param offsets offsets
      * @param topic   topic name
      * @return Array Offset
      */
    def getArrayOffsetRangeWrapper(offsets: Seq[(Int, Long, Long)], topic: String): Array[OffsetRange] = {
        offsets.map {
            case (partitionId: Int, startOffset: Long, stopOffset: Long) ⇒
                OffsetRange(topic, partitionId, startOffset, stopOffset)
        }.toArray
    }

    /**
      * get cluster offset range
      *
      * @return cluster offset range
      *         format : (topic,partitionId,smallestOffset,largestOffset)
      */
    def getClusterOffsetRanges(topics: Set[String]) = {
        val isSmallest = false
        val largest = getTopicPartitionOffsets(topics, isSmallest)
        val smallest = getTopicPartitionOffsets(topics)
        val topicPartitionMap2topicOffset = (topicPartition: Map[TopicAndPartition, Long]) ⇒
            topicPartition.map { case (k, v) ⇒ (k.topic, k.partition) → v }
        val largestMap = topicPartitionMap2topicOffset(largest)
        val smallestMap = topicPartitionMap2topicOffset(smallest)
        for {
            small ← smallestMap
            large ← largestMap if small._1 == large._1
        } yield (small._1._1,small._1._2, small._2, large._2)
    }

    /**
      * get topicPartitionOffset
      *
      * @param topics     topics with a set
      * @param isSmallest get smallest topic
      * @return smallest or largest offset ,default value is smallest
      */
    private def getTopicPartitionOffsets(topics: Set[String], isSmallest: Boolean = true): Map[TopicAndPartition, Long] = {
        (for {
            topicPartitions <- kc.getPartitions(topics).right
            leaderOffsets <- (if (isSmallest) {
                kc.getEarliestLeaderOffsets(topicPartitions)
            } else {
                kc.getLatestLeaderOffsets(topicPartitions) //largest
            }).right
        } yield {
            leaderOffsets.map { case (tp, lo) =>
                (tp, lo.offset)
            }
        }).fold(
            errs => throw new SparkException(errs.mkString("\n")),
            ok => ok
        )
    }

    /**
      * getOffsets by zk and kafka
      *
      * @param groupId
      * @param topicSet
      * @return
      */
    def getConsumerOffsetByZKAndKafka(groupId: String, topicSet: Set[String]): Map[TopicAndPartition, Long] = {
        getConsumerOffsetByZKAndKafka_(groupId, topicSet).map(r => (r._1, r._2._1))
    }

    /**
      * getOffsets by zk and kafka
      *
      * @param groupId
      * @param topicSet
      * @return
      */
    def getConsumerOffsetByZKAndKafka_(groupId: String, topicSet: Set[String]): Map[TopicAndPartition, (Long, Long)] = {
        var fromOffsets: Map[TopicAndPartition, (Long, Long)] = Map[TopicAndPartition, (Long, Long)]()
        val topicAndPartition = kc.getPartitions(topicSet).right.get
        val consumerOffsetFromZK = kc.getConsumerOffsets(groupId, topicAndPartition)
        val tuples = getClusterOffsetRanges(topicSet)
        //没有消费记录
        if (consumerOffsetFromZK.isLeft) {
            for {
                tp <- topicAndPartition
                (topic, partitionId, smallestOffset, largestOffset) <- tuples
                if tp.topic == topic && tp.partition == partitionId
            } fromOffsets += (tp -> (smallestOffset, largestOffset))
            //有消费记录
        } else {
            for {
                (tp, offset) <- consumerOffsetFromZK.right.get
                (topic, partitionId, smallestOffset, largestOffset) <- tuples
                if tp.topic == topic && tp.partition == partitionId
            } fromOffsets += (tp -> (Math.max(offset, smallestOffset), largestOffset))
        }
        fromOffsets
    }

    /**
      * write offsets to ZK
      * @param groupId
      * @param offsets
      * @return
      */
    private def setConsumerOffsets(groupId: String,offsets: Map[TopicAndPartition, Long]) = {
        kc.setConsumerOffsets(groupId,offsets)
    }

    /**
      * 保存offset
      */
    def saveConsumerOffset(topic:String, groupId:String, offsetRanges: Array[OffsetRange]) = {
        var offsets: Map[TopicAndPartition, Long] = Map()
        for (o <- offsetRanges) {
            offsets += (TopicAndPartition(topic,o.partition) -> o.untilOffset)
        }
        kc.setConsumerOffsets(groupId,offsets)
    }

}

object KafkaClusterExtension {

    def apply(kafkaParams: Map[String, String]): KafkaClusterExtension = {
        new KafkaClusterExtension(kafkaParams)
    }

    def apply(kafkaConsumerConfig: Properties): KafkaClusterExtension = {
        new KafkaClusterExtension(kafkaConsumerConfig)
    }

}