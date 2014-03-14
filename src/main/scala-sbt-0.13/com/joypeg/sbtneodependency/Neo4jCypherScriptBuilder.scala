package com.joypeg.sbtneodependency

import sbt.{IO, ModuleID, File}
import scala.util.Try

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
                  companyName: String,
                  extra: Map[String, String]): Try[Unit] = Try {

    IO.write(outFile, getScriptLines(mods, name, org, scalaVer, internalOrgs, companyName, extra).mkString("\n"))
  }

  def getScriptLines(mods: Seq[ModuleID],
                     name: String,
                     org: String,
                     scalaVer: String,
                     internalOrgs: Seq[String],
                     companyName: String,
                     extra: Map[String, String]): Seq[String] = {

    val nameWithTags = makeNameWithExtraLabels(name, companyName, extra)
    val rootnode = Seq(s"""MERGE (a: $nameWithTags {name:"${name}", org:"${org}"});""")
    val nodes = mods.map(createCypherNode(_, internalOrgs, companyName, extra)).sorted
    val relations = mods.map(createCypherRelation(_, name, org, scalaVer)).sorted
    rootnode ++ nodes ++ relations
  }

  private[this] def createCypherNode(m: ModuleID, internalOrgs: Seq[String], companyName: String, extra: Map[String, String]): String = {
    if (internalOrgs.contains(m.organization)){
      val nameWithTags = makeNameWithExtraLabels(m, companyName, extra)
      s"""MERGE (a: $nameWithTags {name:"${m.name}", org:"${m.organization}"});"""
    } else {
      s"""MERGE (a: External {name:"${m.name}", org:"${m.organization}"});"""
    }
  }

  private[this] def createCypherRelation(m: ModuleID,
                                         name: String,
                                         org: String,
                                         scalaVer: String): String = m.crossVersion match {

    case binary if binary.toString == "Binary" =>
      s"""MATCH (a {name:"${m.name}", org:"${m.organization}"}), (b {name:"$name", org:"$org"}) CREATE UNIQUE (b)-[r:Depends {version:"${m.revision}", scalaVersion:"${scalaVer}"}]->(a);"""
    case _ =>
      s"""MATCH (a {name:"${m.name}", org:"${m.organization}"}), (b {name:"$name", org:"$org"}) CREATE UNIQUE (b)-[r:Depends {version:"${m.revision}"}]->(a);"""
  }

  private[this] def makeNameWithExtraLabels(m: ModuleID, companyName: String, extra: Map[String, String]): String = {
    makeNameWithExtraLabels(m.name, companyName, extra)
  }

  private[this] def makeNameWithExtraLabels(name: String, companyName: String, extra: Map[String, String]): String = {
    val labelSfound: Seq[String] = extra.filter(x => name.contains(x._1)).map(x => x._2).toSeq
    (Seq(companyName) ++ labelSfound).mkString("_")
  }

}
