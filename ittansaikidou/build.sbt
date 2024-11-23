import play.sbt.PlayImport.javaJdbc

name := """ittansaikidou"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.15"

libraryDependencies ++= Seq(
    guice,
    javaJdbc,
    "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
    "mysql" % "mysql-connector-java" % "8.0.33"
)