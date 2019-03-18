
import java.util.{ Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import scala.io.Source


class KafkaProducerThread extends Thread{
  override def run():Unit = {
    val topic = "test"
    val brokers = "xuranusMaster:9092,xuranusSlave1:9092,xuranusSlave2:9092"
    val props = new Properties()
    props.put("bootstrap.servers", brokers)
    props.put("group.id", "xuranus")
    props.put("client.id", "ScalaProducerExample")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")

    val producer = new KafkaProducer[String, String](props)

    /********************************************************************************************************/
    println("Start sent simulate.")
    for(i <- 1 to 14) {
      val fileName = "/home/xuranus/spark_homework/article/" + i + ".txt"
      val source = Source.fromFile(fileName)
      val str = source.mkString

      val record = new ProducerRecord[String, String](topic,fileName , str)
      producer.send(record)
      println("produced " + i + " articles!")
      Thread.sleep(2000)
    }

    producer.close()
  }

}
