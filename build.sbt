name := """Magirator API"""
organization := "net.magirator"

version := "0.4-HAPPY"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.4"

libraryDependencies += guice

testOptions += Tests.Argument(TestFrameworks.JUnit, "-a", "-q")