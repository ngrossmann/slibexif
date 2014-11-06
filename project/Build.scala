import sbt._
import Keys._

object LibExifBuild extends Build {
  val dependencies = Seq(
      "org.scalatest" %% "scalatest" % "2.1.7" % "test" cross CrossVersion.binary
  )
  val mySettings = Defaults.defaultSettings ++ Seq(
      libraryDependencies ++= dependencies,
      name := "slibexif",
      organization := "net.n12n.exif",
      scalaVersion in ThisBuild := "2.11.4",
      crossScalaVersions := Seq("2.10.4", "2.11.4"),
      scalacOptions ++= Seq("-deprecation", "-unchecked"),
      testOptions ++= Seq(Tests.Argument("-oSDW")), 
      version in ThisBuild := "0.2.0-SNAPSHOT",
      credentials += Credentials(Path.userHome / ".sbt" / "sonatype"),
      publishTo := {
        val nexus = "https://oss.sonatype.org/"
        if (isSnapshot.value)
          Some("snapshots" at nexus + "content/repositories/snapshots")
        else
          Some("releases"  at nexus + "service/local/staging/deploy/maven2")
      },
      pomExtra := <licenses>
        <license>
          <name>GNU LESSER GENERAL PUBLIC LICENSE, Version 3</name>
          <url>http://www.gnu.org/licenses/lgpl.txt</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>https://github.com/ngrossmann/slibexif</url>
      </scm>
  )
  
  lazy val libexif = Project(id = "slibexif", base = file("."), settings = mySettings)
  
  val exampleSettings = Defaults.defaultSettings ++ Seq(
      libraryDependencies <+= version("net.n12n.exif" %% "slibexif" % _)
  )
  lazy val examples = Project(id = "examples", base = file("examples"),
      settings = exampleSettings)
}
