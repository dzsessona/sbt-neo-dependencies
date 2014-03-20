package com.joypeg.graphdeps

import sbt.Keys._
import sbt.{Plugin, SettingKey}

/**
 * @author Diego Zambelli Sessona
 * @since 14/03/2014 22:36
 */
trait GraphDependencyPlugin extends Plugin {

  val graphDepsCompanyName = SettingKey[String]("graphDepsCompanyName", "Name of the company")
  val graphDepsCompanyOrgs = SettingKey[Seq[String]]("graphDepsCompanyOrgs", "Internal organizations")
  val graphDepsCompanyTags = SettingKey[Map[String,String]]("graphDepsCompanyTags", "Extra labels for the organization")

  /**
   * Utlity method to log the running tasks
   * @param taskName - the name of the task running
   * @param streams - the sbt TaskStream used for logging
   * @param prefix - string to prepend to the task name
   */
  def logTaskHeader(taskName: String, streams: TaskStreams, prefix: String = "Running "): Unit = {
    streams.log.info("\n" + prefix + taskName)
    streams.log.info((for(i <- 1 to (taskName.size + prefix.size)) yield '-').toSeq.mkString)
  }

}
