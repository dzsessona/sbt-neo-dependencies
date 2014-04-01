import com.joypeg.graphdeps.neo4j.Neo4jGraphDependencies

Neo4jGraphDependencies.neo4jDepsSetting

name := "company-db"

organization := "a.company"

version := "1.0"

scalaVersion := "2.10.2"

crossScalaVersions := Seq("2.9.2", "2.10.2")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature")

resolvers += "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/"

val testDependencies = Seq(
  "com.h2database"	% "h2"		% "1.3.166"	% "test"
)

libraryDependencies ++= { Seq(
    "ch.qos.logback"            % "logback-classic"	% "1.0.6",
    "c3p0" 			% "c3p0"		% "0.9.1.2",
    "com.typesafe"              % "config"		% "1.0.2",
    "net.liftweb"       	%% "lift-json"		% "2.5.1",
    "postgresql" 		% "postgresql" 		% "9.1-901.jdbc4",
    "joda-time" 		% "joda-time"		% "2.3"
  ) ++ testDependencies
}

neo4jInternalName := "ACOMPANY"

neo4jInternalOrgs := Seq("a.company")

neo4jTagsLabels  := Map("service" -> "Service")
