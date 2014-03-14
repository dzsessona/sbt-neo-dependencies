package com.joypeg.sbtneodependency

import sbt.{IO, ModuleID, File}

/**
 * @author Diego Zambelli Sessona
 * @since 12/03/2014 17:53
 */
trait Neo4jCypherScriptBuilder extends GraphDependencyPlugin {

  def writeScript(mods: Seq[ModuleID],
                  name: String,
                  org: String,
                  scalaVer: String,
                  internalOrgs: Seq[String],
                  outFile: File,
                  companyName: String): Unit = {

    IO.write(outFile, getScriptLines(mods, name, org, scalaVer, internalOrgs, companyName).mkString("\n"))
  }

  def getScriptLines(mods: Seq[ModuleID],
                     name: String,
                     org: String,
                     scalaVer: String,
                     internalOrgs: Seq[String],
                     companyName: String): Seq[String] = {

    val rootnode = Seq("MERGE (a: " + companyName + " {name:\"" + name + "\", org:\"" + org +"\"});")
    val nodes = mods.map(createCypherNode(_, internalOrgs, companyName)).sorted
    val relations = mods.map(createCypherRelation(_, name, org, scalaVer)).sorted
    rootnode ++ nodes ++ relations
  }

  private[this] def createCypherNode(m: ModuleID, internalOrgs: Seq[String], companyName: String): String = {
    if (internalOrgs.contains(m.organization)){
      "MERGE (a: " + companyName + " {name:\"" + m.name + "\", org:\"" + m.organization +"\"});"
    } else {
      "MERGE (a: External {name:\"" + m.name + "\", org:\"" + m.organization +"\"});"
    }
  }

  private[this] def createCypherRelation(m: ModuleID,
                                         name: String,
                                         org: String,
                                         scalaVer: String): String = m.crossVersion match {

    case binary if binary.toString == "Binary" =>
      "MATCH (a {name:\"" + m.name + "\", org:\"" + m.organization +"\"}), (b {name:\"" + name + "\", org:\"" + org + "\"}) CREATE UNIQUE (b)-[r:Depends {version:\"" + m.revision + "\", scalaVersion:\"" + scalaVer +"\"}]->(a);"
    case _ =>
      "MATCH (a {name:\"" + m.name + "\", org:\"" + m.organization +"\"}), (b {name:\"" + name + "\", org:\"" + org + "\"}) CREATE UNIQUE (b)-[r:Depends {version:\"" + m.revision + "\"}]->(a);"

  }


}
