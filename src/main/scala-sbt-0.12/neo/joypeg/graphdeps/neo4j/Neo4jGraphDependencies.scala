package com.joypeg.graphdeps.neo4j

import com.joypeg.graphdeps.{Neo4jData, CurrentModule, GraphDependencyPlugin}
import sbt._
import Keys._

/**
* @author Diego Zambelli Sessona
* @since 14/03/2014 23:48
*/
object Neo4jGraphDependencies extends GraphDependencyPlugin with Neo4jCyperScriptMaker {

  val neo4jInternalName = SettingKey[String]("neoDepInternalName", "Name of the company")
  val neo4jInternalOrgs = SettingKey[Seq[String]]("neoDepInternalOrgs", "Internal organizations")
  val neo4jTagsLabels   = SettingKey[Map[String,String]]("neoDepExtraLabels", "Extra labels for the organization")
  val neo4jCypherScript = SettingKey[File]("neo4jCypherScript", "The cypher script file generated")
  val neo4jLoadResults  = SettingKey[File]("neo4jLoadResults", "Result of loading the data in neo4j")

  val neo4jWriteDependencies = TaskKey[Unit]("neo4jWriteDependencies", "Write the cypher script file")
  val neo4jShowDependencies  = TaskKey[Unit]("neo4jShowDependencies", "Show all nodes and relations in console")
  val neo4jLoadDependencies  = TaskKey[Unit]("neo4jLoadDependencies", "Write the cypher script file and load data")

  neo4jCypherScript <<= (baseDirectory in Compile)
  neo4jLoadResults  <<= (baseDirectory in Compile)

  lazy val neo4jDepsSetting = Seq(
    neo4jInternalName := "Company",
    neo4jInternalOrgs := Seq.empty,
    neo4jTagsLabels   := Map.empty,
    neo4jLoadDependencies  <<= loadNodesAndRelations.dependsOn(neo4jWriteDependencies),
    neo4jShowDependencies  <<= showNodesAndRelations,
    neo4jWriteDependencies <<= writeNodesAndRelations
  )

  private[this] def loadNodesAndRelations = (neo4jCypherScript, neo4jLoadResults, streams).map {
    (cypher, results, s) => {
      logTaskHeader("neo4jLoadDependencies", s)
      try { getSysProperty("neo4j.home") match {
          case None =>
            s.log.error("Cannot find neo4j.home in your system settings")
          case Some(neosh) =>
            val exitCdode = "%s/bin/neo4j-shell -file %s > %s".format(
              neosh, cypher.getAbsolutePath, results.getAbsolutePath
            ).!
            s.log.info("neo4j-shell process terminated with Exit code: " + exitCdode)
        }
      } catch {
        case ex => s.log.error(ex.getMessage)
      }
    }
  }

  private[this] def writeNodesAndRelations = (libraryDependencies, name, organization, scalaBinaryVersion,
                                              neo4jInternalName, neo4jInternalOrgs, neo4jTagsLabels,
                                              streams, neo4jCypherScript, neo4jLoadResults).map {
    (modules, name, org, scalaBinVer, neoName, neoOrg, neoTags, s, script, results) =>
      logTaskHeader("neo4jWriteDependencies", s)
      val scriptoutput = script / "neodependencies" / "nodesandrelations.cyp"
      try { createAndWriteCyperScript(
          modules, CurrentModule(name, org, scalaBinVer),
          Neo4jData(neoName, neoOrg, neoTags), scriptoutput
        )
        s.log.info("Cypher script wrote in: %s".format(scriptoutput.getAbsolutePath))
      } catch {
        case ex => s.log.error(ex.getMessage)
      }
  }

  private[this] def showNodesAndRelations = (libraryDependencies, name, organization, scalaBinaryVersion,
                                             neo4jInternalName, neo4jInternalOrgs, neo4jTagsLabels, streams).map {
    (modules, name, org, scalaBinVer, neoName, neoOrg, neoTags, s) => {
      logTaskHeader("neo4jShowDependencies", s)
      getScriptLines(modules, CurrentModule(name, org, scalaBinVer), Neo4jData(neoName, neoOrg, neoTags))
        .foreach(s.log.info(_))
    }
  }

}
