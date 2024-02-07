ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.12"


lazy val root = (project in file("."))
  .settings(
    name := "ColdVacation",
    idePackagePrefix := Some("redstone.cv")
  )

val chiselVersion = "6.0.0"
addCompilerPlugin("org.chipsalliance" % "chisel-plugin" % chiselVersion cross CrossVersion.full)
libraryDependencies += "org.chipsalliance" %% "chisel" % chiselVersion
