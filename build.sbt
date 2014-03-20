import sbtrelease.ReleasePlugin.ReleaseKeys._

sbtPlugin := true

name := "sbt-neo-dependencies"

organization := "github.com.dzsessona"

description := "SBT plugin to import dependencies of an sbt project into neo4j"

crossBuildingSettings

CrossBuilding.crossSbtVersions := Seq("0.12", "0.13")

unmanagedSourceDirectories in Compile <+= (sourceDirectory in Compile, sbtVersion in sbtPlugin) {
  (src,ver) => src / ("scala-sbt-" + ver)
}

publishArtifact in Test := false

publishMavenStyle := true

pomIncludeRepository := { _ => false }

licenses := Seq("GPL-2.0" -> url("http://opensource.org/licenses/GPL-2.0"))

homepage := Some(url("https://github.com/dzsessona/sbt-neo-dependencies"))

pgpPublicRing := file("/home/me/pgp/pubring.asc")

publishTo <<= version { (v: String) =>
  val nexus = "https://oss.sonatype.org/"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <scm>
    <url>git@github.com:dzsessona/sbt-neo-dependencies.git</url>
    <connection>scm:git@github.com:dzsessona/sbt-neo-dependencies.git</connection>
  </scm>)

releaseSettings
