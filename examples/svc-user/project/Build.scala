import sbt._
import Keys._
import com.joypeg.graphdeps.neo4j.Neo4jGraphDependencies
import com.joypeg.graphdeps.neo4j.Neo4jGraphDependencies._

object BuildSettings {

  val neoSettings = neo4jDepsSetting ++ Seq(
    neo4jInternalName := "ACOMPANY",
    neo4jInternalOrgs := Seq("a.company"),
    neo4jTagsLabels   := Map("client" -> "Service_Client", "web" -> "Web")
  )

  val commonDependencies = Seq(
    "ch.qos.logback" % "logback-classic" % "1.0.6",
    "c3p0"           % "c3p0"            % "0.9.1.2"
  )
    
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "a.company",
    version := "1.0",
    scalaVersion := "2.10.3",
    crossScalaVersions := Seq("2.9.2", "2.10.2"),
    libraryDependencies ++= commonDependencies
  ) ++ neoSettings
}

object MyBuild extends Build {
  import BuildSettings._

  lazy val root: Project = Project(
    "svc-user",
    file("."),
    settings = buildSettings
  ) aggregate(core, server, client)

  lazy val core: Project = Project(
    "svc-user-core",
    file("core"),
    settings = buildSettings
  )

  lazy val server: Project = Project(
    "svc-user-server",
    file("server"),
    settings = buildSettings
  ).dependsOn(core)

  lazy val client: Project = Project(
    "svc-user-client",
    file("client"),
    settings = buildSettings
  ).dependsOn(core)

}