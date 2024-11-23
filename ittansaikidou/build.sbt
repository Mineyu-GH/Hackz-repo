import play.sbt.PlayImport.javaJdbc

name := """ittansaikidou"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)
  .enablePlugins(DockerPlugin)
  .settings(
      Universal / javaOptions ++= Seq("-Dpidfile.path=/dev/null"),
//      dockerBaseImage := "eclipse-temurin:17",
      dockerExposedPorts ++= Seq(9000)
  )

scalaVersion := "2.13.15"

libraryDependencies ++= Seq(
    guice,
    "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
)
