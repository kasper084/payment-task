ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.7"

lazy val root = (project in file("."))
  .settings(
    name := "payment-task"
  )

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.2.7",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.7",
  "com.softwaremill.sttp.tapir" %% "tapir-core" % "0.19.0-M13",
  "org.typelevel" %% "cats-core" % "2.3.0",
  "com.github.pureconfig" %% "pureconfig" % "0.17.0"
)

scalacOptions += "-Ypartial-unification"