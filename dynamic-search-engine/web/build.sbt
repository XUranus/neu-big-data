name := "play_ui"
 
version := "1.0" 
      
lazy val `play_ui` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
      
scalaVersion := "2.11.11"

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )




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
libraryDependencies += "org.apache.hbase" % "hbase-client" % "2.1.1"

