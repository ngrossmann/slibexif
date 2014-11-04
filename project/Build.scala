import sbt._
import Keys._
import sbtrelease._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleaseStateTransformations._

object LibExifBuild extends Build {
  val dependencies = Seq(
      "org.scalatest" %% "scalatest" % "2.1.7" % "test" cross CrossVersion.binary
  )
  val mySettings = Defaults.defaultSettings ++ releaseSettings ++ Seq(
      libraryDependencies ++= dependencies,
      name := "libexif",
      organization := "net.n12n.exif",
      scalaVersion in ThisBuild := "2.11.4",
      crossScalaVersions := Seq("2.10.4", "2.11.4"),
      scalacOptions ++= Seq("-deprecation", "-unchecked"),
      testOptions ++= Seq(Tests.Argument("-oSDW")), 
      version in ThisBuild := "0.2.0-SNAPSHOT",
      pomExtra := <licenses>
        <license>
          <name>GNU LESSER GENERAL PUBLIC LICENSE, Version 3</name>
          <url>http://www.gnu.org/licenses/lgpl.txt</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>https://github.com/ngrossmann/libexif</url>
      </scm>,
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
