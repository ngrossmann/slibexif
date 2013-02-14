import sbt._
import Keys._
import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleaseStateTransformations._

object LibExifBuild extends Build {
  val dependencies = Seq(
      "org.scalatest" %% "scalatest" % "2.0.M5" % "test" cross(CrossVersion.full)
  )
  val mySettings = Defaults.defaultSettings ++ releaseSettings ++ Seq(
      libraryDependencies ++= dependencies,
      name := "libexif",
      organization := "net.n12n.exif",
      scalaVersion in ThisBuild := "2.9.2",
      crossScalaVersions := Seq("2.9.2", "2.10.0"),
      scalacOptions ++= Seq("-deprecation", "-unchecked"),
      testOptions ++= Seq(Tests.Argument("-oSDW")), 
      version in ThisBuild := "0.1.0-SNAPSHOT",
      ReleaseKeys.releaseProcess :=  Seq[ReleaseStep](
    		  checkSnapshotDependencies,
    		  inquireVersions, 
    		  runTest,
    		  setReleaseVersion, 
    		  commitReleaseVersion,
    		  tagRelease,
    		  // publishArtifacts,
    		  setNextVersion,
    		  commitNextVersion,
    		  pushChanges)
  )
  
  lazy val libexif = Project(id = "libexif", base = file("."), settings = mySettings)
  
  val exampleSettings = Defaults.defaultSettings ++ Seq(
      libraryDependencies <+= version("net.n12n.exif" %% "libexif" % _)
  )
  lazy val examples = Project(id = "examples", base = file("examples"),
      settings = exampleSettings)
}