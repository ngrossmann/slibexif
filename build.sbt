
name := "libexif"

description := "Library to access EXIF information in JPEG files"

organization := "de.n12n.exif"

version := "0.1.0-SNAPSHOT"

scalaVersion := "2.9.2"

scalacOptions += "-deprecation"

libraryDependencies += "org.scalatest" %% "scalatest" % "1.6.1" % "test"

testOptions in Test += Tests.Argument("-oSDW")
