package org.apache.spark.sql.execution.datasources.kafka

import com.twitter.bijection.Injection
import com.twitter.bijection.avro.GenericAvroCodecs
import kafka.serializer.Decoder
import kafka.utils.VerifiableProperties
import org.apache.avro.Schema
import org.apache.avro.generic.GenericRecord

class AvroDecoder(props: VerifiableProperties = null) extends Decoder[String] {

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

  val schema: Schema = new Schema.Parser().parse(I4_SCHEMA)
  val recordInjection: Injection[GenericRecord, Array[Byte]] = GenericAvroCodecs.toBinary(schema)

  def fromBytes(bytes: Array[Byte]): String = {
    recordInjection.invert(bytes).get.get("data").toString
  }
}
