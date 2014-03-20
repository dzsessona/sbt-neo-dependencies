package com.joypeg.graphdeps.neo4j

import com.joypeg.graphdeps.{CurrentModule, Neo4jData}
import sbt.{IO, ModuleID, File}
import scala.util.Try

/**
 * @author Diego Zambelli Sessona
 * @since 15/03/2014 00:46
 */
trait Neo4jCyperScriptMaker {

  def createAndWriteCyperScript(modules: Seq[ModuleID],
                                thisModule: CurrentModule,
                                neoData: Neo4jData,
                                scriptOutputFile: File): Try[Unit] = Try {
    IO.write(scriptOutputFile, getScriptLines(modules, thisModule, neoData).mkString("\n"))
  }

  def getScriptLines(modules: Seq[ModuleID], thisModule: CurrentModule, neoData: Neo4jData): Seq[String] = {

    val nameWithTags = makeNameFindingTags(thisModule.name, neoData.neo4jInternalName, neoData.neo4jTagsLabels)
    val rootnode = Seq(s"""MERGE (a: $nameWithTags {name:"${thisModule.name}", org:"${thisModule.org}"});""")
    val nodes = modules.map(createCypherNode(_, neoData)).sorted
    val relations = modules.map(createCypherRelation(_, thisModule)).sorted
    rootnode ++ nodes ++ relations
  }

  private[this] def createCypherNode(module: ModuleID, neoData: Neo4jData): String = {

    if (neoData.neo4jInternalOrgs.contains(module.organization)){
      val nameWithTags = makeNameFindingTags(module, neoData.neo4jInternalName, neoData.neo4jTagsLabels)
      s"""MERGE (a: $nameWithTags {name:"${module.name}", org:"${module.organization}"});"""
    } else {
      s"""MERGE (a: External {name:"${module.name}", org:"${module.organization}"});"""
    }
  }

  private[this] def createCypherRelation(m: ModuleID, thisModule: CurrentModule): String = m.crossVersion match {

    case binary if binary.toString == "Binary" =>
      s"""MATCH (a {name:"${m.name}", org:"${m.organization}"}), (b {name:"${thisModule.name}", org:"${thisModule.org}"}) """ +
      s"""CREATE UNIQUE (b)-[r:Depends {version:"${m.revision}", scalaVersion:"${thisModule.scalaVersion}"}]->(a);"""
    case _ =>
      s"""MATCH (a {name:"${m.name}", org:"${m.organization}"}), (b {name:"${thisModule.name}", org:"${thisModule.org}"}) """ +
      s"""CREATE UNIQUE (b)-[r:Depends {version:"${m.revision}"}]->(a);"""
  }

}
