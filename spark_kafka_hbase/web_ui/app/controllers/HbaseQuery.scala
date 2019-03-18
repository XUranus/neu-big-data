package controllers
import org.apache.hadoop.hbase.{HBaseConfiguration, TableName}
import org.apache.hadoop.hbase.client._
import org.apache.hadoop.hbase.util.Bytes

object HbaseQuery {
  def searchKeyWord(word:String) ={
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("hbase.zookeeper.quorum", "zookeeper001,zookeeper002,zookeeper003")
    //configuration.addResource("/home/xuranus/hdfs-site.xml");
    conf.set("hbase.master", "xuranusMaster:16000")
    val connection = ConnectionFactory.createConnection(conf)
    val table = connection.getTable(TableName.valueOf("articles"))
    val get= new Get(Bytes.toBytes(word))
    val result=table.get(get)
    val res = result.rawCells().
      map(cells=>(new String(cells.getQualifierArray()),Integer.parseInt(new String(cells.getValueArray()))))
    connection.close()
    res
  }

  def getArticle(index:String) = {
    val conf = HBaseConfiguration.create()
    conf.set("hbase.zookeeper.property.clientPort", "2181")
    conf.set("hbase.zookeeper.quorum", "zookeeper001,zookeeper002,zookeeper003")
    //configuration.addResource("/home/xuranus/hdfs-site.xml");
    conf.set("hbase.master", "xuranusMaster:16000")
    val connection = ConnectionFactory.createConnection(conf)
    val table = connection.getTable(TableName.valueOf("articles_asset"))
    val get= new Get(Bytes.toBytes(index))
    val result=table.get(get)
    val value=result.getValue(Bytes.toBytes("articlesCF"),Bytes.toBytes("content"))
    connection.close()
    new String(value)
  }
}
