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
<<<<<<< HEAD
    "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test
)
=======
    javaJdbc,
    "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test,
    "mysql" % "mysql-connector-java" % "8.0.33",
)
>>>>>>> d371ccd1b23d251d0c8b88d8a39ac72bd71e46ce
