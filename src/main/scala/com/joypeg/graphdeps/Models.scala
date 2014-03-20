package com.joypeg.graphdeps

/**
 * @author Diego Zambelli Sessona
 * @since 20/03/2014 01:14
 */
case class Neo4jData(neo4jInternalName: String,
                     neo4jInternalOrgs: Seq[String],
                     neo4jTagsLabels: Map[String, String])

case class CurrentModule(name: String, org: String, scalaVersion: String)