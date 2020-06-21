import Dependencies._

ThisBuild / scalaVersion     := "2.13.1"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "ar.com.flow"
ThisBuild / organizationName := "Flow"

lazy val root = (project in file("."))
  .settings(
    name := "scala-object-oriented-bike-rental-model",
    libraryDependencies += scalaTest % Test
  )

libraryDependencies += "com.google.guava" % "guava" % "28.1-jre"
