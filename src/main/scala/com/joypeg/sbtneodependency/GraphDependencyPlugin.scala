package com.joypeg.sbtneodependency

import sbt._
import sbt.Keys.TaskStreams

/**
 * @author Diego Zambelli Sessona
 * @since 12/03/2014 17:56
 */
trait GraphDependencyPlugin extends Plugin {

  def logNeoDependencyHeader(taskName: String, streams: TaskStreams): Unit = {
    val separator: String = (for(i <- 1 to (taskName.size + 8)) yield '=').toList.mkString
    streams.log.info("\nRunning " + taskName)
    streams.log.info(separator)
  }

}
