package com.joypeg.graphdeps.neo4j

import com.joypeg.graphdeps.{CurrentModule, Neo4jData}
import sbt.{IO, ModuleID, File}

/**
* @author Diego Zambelli Sessona
* @since 15/03/2014 00:46
*/
trait Neo4jCyperScriptMaker {

  def createAndWriteCyperScript(modules: Seq[ModuleID],
                                thisModule: CurrentModule,
                                neoData: Neo4jData,
                                scriptOutputFile: File): Unit = {
    IO.write(scriptOutputFile, getScriptLines(modules, thisModule, neoData).mkString("\n"))
  }

  def getScriptLines(modules: Seq[ModuleID], thisModule: CurrentModule, neoData: Neo4jData): Seq[String] = {

    val nameWithTags = makeNameFindingTags(thisModule.name, neoData.neo4jInternalName, neoData.neo4jTagsLabels)
    val rootnode = Seq("MERGE (a: %s {name:\"%s\", org:\"%s\"});".format(nameWithTags, thisModule.name, thisModule.org))
    val nodes = modules.map(createCypherNode(_, neoData)).sorted
    val relations = modules.map(createCypherRelation(_, thisModule)).sorted
    rootnode ++ nodes ++ relations
  }

  private[this] def createCypherNode(module: ModuleID, neoData: Neo4jData): String = {

    if (neoData.neo4jInternalOrgs.contains(module.organization)){
      val nameWithTags = makeNameFindingTags(module.name, neoData.neo4jInternalName, neoData.neo4jTagsLabels)
      "MERGE (a: %s {name:\"%s\", org:\"%s\"});".format(nameWithTags, module.name, module.organization)
    } else {
      "MERGE (a: External {name:\"%s\", org:\"%s\"});".format(module.name, module.organization)
    }
  }

  private[this] def createCypherRelation(m: ModuleID, thisModule: CurrentModule): String = m.crossVersion match {

    case binary if binary.toString == "Binary" =>
      ("MATCH (a {name:\"%s\", org:\"%s\"}), (b {name:\"%s\", org:\"%s\"}) " +
       "CREATE UNIQUE (b)-[r:Depends {version:\"%s\", scalaVersion:\"%s\"}]->(a);").format(
        m.name, m.organization, thisModule.name, thisModule.org, m.revision, thisModule.scalaVersion
      )
    case _ =>
      ("MATCH (a {name:\"%s\", org:\"%s\"}), (b {name:\"%s\", org:\"%s\"}) " +
       "CREATE UNIQUE (b)-[r:Depends {version:\"%s\"}]->(a);").format(
        m.name, m.organization, thisModule.name, thisModule.org, m.revision
      )
  }

}
