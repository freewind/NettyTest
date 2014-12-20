import _root_.sbt.Keys._

name := "NettyTest"

version := "1.0"

scalaVersion := "2.11.3"

resolvers ++= Seq(
  "koala" at "http://nexus.openkoala.org/nexus/content/groups/Koala-release",
  "oschina" at "http://maven.oschina.net/content/groups/public",
  "Scalaz" at "http://dl.bintray.com/scalaz/releases",
  "spray repo" at "http://repo.spray.io",
  "ibiblio" at "http://mirrors.ibiblio.org/pub/mirrors/maven2",
  "Sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
  "Sonatype releases" at "http://oss.sonatype.org/content/repositories/releases",
  "akka" at "http://repo.akka.io",
  "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/"
)

libraryDependencies ++= Seq(
  "org.json4s" %% "json4s-native" % "3.2.11",
  "org.json4s" %% "json4s-core" % "3.2.11",
  "org.json4s" %% "json4s-ext" % "3.2.11",
  "ch.qos.logback" % "logback-classic" % "1.1.2",
  "org.slf4j" % "slf4j-api" % "1.7.7",
  "org.specs2" %% "specs2" % "2.4.11" % "test",
  "io.netty" % "netty-all" % "5.0.0.Alpha1"
)
