import com.joypeg.sbtneodependency.{GraphDependencyPlugin, Neo4jCypherScriptBuilder}
import sbt._
import Keys._

/**
 * @author Diego Zambelli Sessona
 * @since 12/03/2014 17:51
 */
object NeoDependencies extends GraphDependencyPlugin with Neo4jCypherScriptBuilder {

  val neoDepInternalOrgs = SettingKey[Seq[String]]("neoDepInternalOrgs", "Internal organizations")
  val neoDepInternalName = SettingKey[String]("neoDepInternalName", "Name of the company")
  val neoDepNeo4jShell   = SettingKey[String]("neoDepNeo4jShell", "Absolute Path to neo4j-shell installation")
  val neoDepCypherOutput = SettingKey[File]("neoDepCypherOutput", "Output file of cypher script")
  val neoDepCypherResult = SettingKey[File]("neoDepCypherResult", "Result of loading the data in neo4j")

  val neoDependencies      = TaskKey[Unit]("neoDependencies", "Write the cypher script file and load data")
  val neoDependenciesShow  = TaskKey[Unit]("neoDependenciesShow", "Show all dependencies in the console")
  val neoDependenciesWrite = TaskKey[Unit]("neoDependenciesWrite", "Write the cypher script file but don't load the data")


  override lazy val settings = Seq(
    neoDepInternalOrgs := Seq.empty,
    neoDepInternalName := "Company",
    neoDepNeo4jShell   := "/Users/dzsessona/Installed/neo4j-community-2.0.1/bin/neo4j-shell",
    neoDepCypherOutput := (baseDirectory in Compile).value / "neodependencies" / "neodependencies.cyp",
    neoDepCypherResult := (baseDirectory in Compile).value / "neodependencies" / "results.txt",
    neoDependencies      <<= loadNodesAndRelations.dependsOn(neoDependenciesWrite),
    neoDependenciesShow  <<= showNodesAndRelations,
    neoDependenciesWrite <<= writeNodesAndRelations
  )

  private[this] def loadNodesAndRelations = (neoDepCypherOutput, neoDepCypherResult, streams, neoDepNeo4jShell).map {
    (script, res, s, neoshell) => {
      logNeoDependencyHeader("neoDependencies", s)
      val ret = s"$neoshell -file ${script.getAbsolutePath} > ${res.getAbsolutePath}".!!
      s.log.info(ret)
    }
  }

  private[this] def writeNodesAndRelations = (libraryDependencies, scalaBinaryVersion, name, organization, neoDepInternalOrgs, neoDepCypherOutput, neoDepInternalName, streams) map {
    (libDeps, scalaBinVer, name, organization, internalorgs, outDir, orgname, s) =>
      logNeoDependencyHeader("neoDependenciesWrite", s)
      writeScript(libDeps, name, organization, scalaBinVer, internalorgs, outDir, orgname).map { res =>
        s.log.info(s"Cypher script wrote in: ${outDir.getAbsolutePath}")
      }.recover {
        case ex => s.log.error(ex.getMessage)
      }.get
  }

  private[this] def showNodesAndRelations = (libraryDependencies, scalaBinaryVersion, name, organization, neoDepInternalOrgs, neoDepInternalName, streams) map {
    (libDeps, scalaBinVer, name, organization, internalorgs, orgname, s) =>
      logNeoDependencyHeader("neoDependenciesShow", s)
      getScriptLines(libDeps, name, organization, scalaBinVer, internalorgs, orgname).foreach(s.log.info(_))
  }


}

