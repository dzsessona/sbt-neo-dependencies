import SonatypeKeys._

sonatypeSettings

sbtPlugin := true

name := "sbt-neo-dependencies"

organization := "com.github.dzsessona"

profileName := "com.github.dzsessona.sbt-neo-dependencies"

description := "SBT plugin to import dependencies of an sbt project into neo4j"

crossBuildingSettings

CrossBuilding.crossSbtVersions := Seq("0.12", "0.13")

unmanagedSourceDirectories in Compile <+= (sourceDirectory in Compile, sbtVersion in sbtPlugin) {
  (src,ver) => src / ("scala-sbt-" + ver)
}

publishArtifact in Test := false

publishMavenStyle := true

pomIncludeRepository := { _ => false }

pgpPublicRing := file("/Users/dzsessona/Documents/mykeys/diegopgp.asc")

//publishTo <<= version { (v: String) =>
//  val nexus = "https://oss.sonatype.org/"
//  if (v.trim.endsWith("SNAPSHOT"))
//    Some("snapshots" at nexus + "content/repositories/snapshots")
//  else
//    Some("releases"  at nexus + "service/local/staging/deploy/maven2")
//}

pomExtra := (
  <url>http://github.com/dzsessona/sbt-neo-dependencies</url>
  <licenses>
    <license>
      <name>GPL-2.0"</name>
      <url>http://opensource.org/licenses/GPL-2.0"</url>
    </license>
  </licenses>
  <scm>
    <connection>scm:git:github.com/dzsessona/sbt-neo-dependencies.git</connection>
    <developerConnection>scm:git:git@github.com:dzsessona/sbt-neo-dependencies.git</developerConnection>
    <url>github.com/dzsessona/sbt-neo-dependencies</url>
  </scm>
  <developers>
    <developer>
      <id>dzsessona</id>
      <name>Diego Zambelli Sessona</name>
      <url>https://www.linkedin.com/in/diegozambellisessona</url>
    </developer>
  </developers>
)
