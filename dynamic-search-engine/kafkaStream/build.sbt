name := "realtimeHotspot"

version := "1.0"

scalaVersion := "2.12.8"

libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"

libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.0"
libraryDependencies += "org.apache.hbase" % "hbase-client" % "2.1.1"


libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.0"
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0"

libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.7"
libraryDependencies += "org.apache.flume" % "flume-ng-core" % "1.8.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.0"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.12" % "2.8.7"
//libraryDependencies += "com.huaban" % "jieba-analysis" % "1.0.2"

libraryDependencies += "org.apache.hadoop" % "hadoop-core" % "1.2.1"
libraryDependencies += "org.apache.hbase" % "hbase" % "2.1.1" pomOnly()

resolvers += "Apache HBase" at "https://repository.apache.org/content/repositories/releases"

resolvers += "Thrift" at "https://people.apache.org/~rawson/repo/"

libraryDependencies ++= Seq(
  "org.apache.hadoop" % "hadoop-core" % "0.20.2",
  "org.apache.hbase" % "hbase" % "0.90.4"
)

libraryDependencies += "org.apache.hbase" % "hbase-client" % "2.1.1"
libraryDependencies += "org.apache.hbase" % "hbase-common" % "2.1.1"
libraryDependencies += "org.apache.hbase" % "hbase-server" % "2.1.1"
