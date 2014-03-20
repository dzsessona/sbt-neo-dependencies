package com.joypeg.graphdeps

import sbt.ModuleID

/**
 * @author Diego Zambelli Sessona
 * @since 14/03/2014 23:24
 */
package object neo4j {

  def makeNameFindingTags(m: ModuleID, company: String, tags: Map[String, String]): String = {
    makeNameFindingTags(m.name, company, tags)
  }

  def makeNameFindingTags(name: String, company: String, tags: Map[String, String]): String = {
    val tagFound: Seq[String] = tags.filter(x => name.contains(x._1)).map(x => x._2).toSeq
    (Seq(company) ++ tagFound).mkString("_")
  }

  def getEnvProperty(key: String): Option[String] = {
    Option(System.getenv(key))
  }
}
