import org.apache.log4j.Logger
import org.apache.log4j.Level
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client.{ConnectionFactory, Put}
import org.apache.hadoop.hbase.util.Bytes
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

class KafkaStreamingConsumer extends Thread{
  Logger.getLogger("org").setLevel(Level.OFF)
  Logger.getLogger("akka").setLevel(Level.OFF)


  @transient
  def insert(word:String,index:String,count:String): Unit ={
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("hbase.zookeeper.quorum", "zookeeper001,zookeeper002,zookeeper003")
    conf.set("hbase.master", "xuranusMaster:16000")
    val connection = ConnectionFactory.createConnection(conf)
    val admin = connection.getAdmin()

    val table = connection.getTable(TableName.valueOf("articles"))
    val theput= new Put(Bytes.toBytes(word))
    theput.addColumn(Bytes.toBytes("articlesCF"), Bytes.toBytes(index), Bytes.toBytes(count))
    table.put(theput)
  }


  override def run()= {
    System.setProperty("hadoop.home.dir", "/home/xuranus/Desktop/hadoop/hadoop-2.7.6")
    System.setProperty("log4j.rootCategory", "ERROR")
    val conf = new SparkConf().setAppName("app").setMaster("local[*]")
    val ssc = new StreamingContext(conf,Seconds(2))
    val kafkaParam = Map(
      "metadata.broker.list" -> "xuranusMaster:9092,xurausSlave01:9092,xuranusSlave2:9092",
      "bootstrap.servers" -> "xuranusMaster:9092",
      "key.deserializer" -> classOf[StringDeserializer],
      "value.deserializer" -> classOf[StringDeserializer],
      "auto.offset.reset" -> "earliest",
      "group.id" -> "tweet-consumer"
    )

    val preferredHosts = LocationStrategies.PreferConsistent

    val topics = Array("test")
    //    获取日志数据
    val stream =
      KafkaUtils.createDirectStream[String, String](
      ssc,
      preferredHosts,
      ConsumerStrategies.Subscribe[String, String](topics, kafkaParam)
    )

    stream.map(record=>(record.key().toString,record.value().toString))
      .foreachRDD(rdd=>{
      rdd.foreachPartition(partition=>{
        partition.foreach(s=>{
          val path = s._1
          val content = s._2
          val wordCounts = content //清洗数据
            .replaceAll("[0123456789]", " ")
            .replaceAll("\\[", " ")
            .replaceAll("\\]", " ")
            .replaceAll("[,.:;]", " ")
            .replaceAll("\"", " ")
            .replaceAll("'", " ")
            .replaceAll("\n", " ")
            .replaceAll("\\(", " ")
            .replaceAll("\\)", " ")
            .toLowerCase()
            .split(" "). //词频统计
            map((_,1)).groupBy(_._1).map(x=>(x._1,x._2.length))

          wordCounts.filter(pair=>pair._1.length>0).foreach((pair)=>{
            val word = pair._1
            val count = pair._2+""
            val index = path
            println((word,index,count)) //打印准备创建的索引

            val conf = HBaseConfiguration.create()
            conf.set("hbase.zookeeper.property.clientPort", "2181")
            conf.set("hbase.zookeeper.quorum", "zookeeper001,zookeeper002,zookeeper003")
            conf.set("hbase.master", "xuranusMaster:16000")
            val connection = ConnectionFactory.createConnection(conf)

            //持久化关键字索引
            val table1 = connection.getTable(TableName.valueOf("articles"))
            val put1= new Put(Bytes.toBytes(word))
            put1.addColumn(Bytes.toBytes("articlesCF"), Bytes.toBytes(index), Bytes.toBytes(count))
            table1.put(put1)

            //持久化文本资源
            val table2 = connection.getTable(TableName.valueOf("articles_asset"))
            val put2= new Put(Bytes.toBytes(index))
            put2.addColumn(Bytes.toBytes("articlesCF"), Bytes.toBytes("content"), Bytes.toBytes(content))
            table2.put(put2)

            connection.close()
          })

        })
      })
    })

    ssc.start()
    ssc.awaitTermination()
  }


}
