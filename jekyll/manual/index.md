---
layout: manual
active: manual
title: Sbt Neo Dependencies - manual
---

<h2 id="installation">Installation</h2>

**The artifacts of this plugin are uploaded in Maven Central.** 

As from the [sbt documentation](http://www.scala-sbt.org/release/docs/Detailed-Topics/Resolvers.html) 
the DefaultMavenRepository is the main Maven repository at http://repo1.maven.org/maven2/ and is included by default. 
Therefore you don't need to add any resolvers to your build definition; you can simply add the plugin the *project/plugins.sbt* file as follow: 

``` addSbtPlugin("com.github.dzsessona" %% "sbt-neo-dependencies" % "version")  ```

Replace **version** with the last version of this plugin. All versions for this plugin can be found [here]({{ site.url }}/versions). In order to enable
the tasks of this plugin in your project you also have to modify your build definition; An sbt build definition can contain files ending in .sbt, located in the base directory, 
and files ending in .scala, located in the project/ subdirectory the base directory. In case you are confused about the difference between build.sbt and Build.scala read [this](http://www.scala-sbt.org/release/docs/Getting-Started/Full-Def.html).


<h3 id="buildsbt" style="margin-top: 20px;">build.sbt</h4>

In your build definition you need to import the settings defined in the plugin, and override the settings keys for **neo4jInternalName, neo4jInternalOrgs, neo4jTagsLabels** as in this example snippet:

```
import com.joypeg.graphdeps.neo4j.Neo4jGraphDependencies

Neo4jGraphDependencies.neo4jDepsSetting

neo4jInternalName := "ACOMPANY"

neo4jInternalOrgs := Seq("a.company")

neo4jTagsLabels  := Map("service" -> "Service")
```

Look up the section **Settings** for more information about what each of this setting represent.

<h3 id="buildscala" style="margin-top: 20px;">project/Build.scala</h4>

If you have a full definition (maybe with multiple subproject) you can import the settings defined 
in the plugin (and override the settings keys for **neo4jInternalName, neo4jInternalOrgs, neo4jTagsLabels**) for all the
subproject as follow:

```
import sbt._
import Keys._
import com.joypeg.graphdeps.neo4j.Neo4jGraphDependencies
import com.joypeg.graphdeps.neo4j.Neo4jGraphDependencies._

object BuildSettings {

  val neoSettings = neo4jDepsSetting ++ Seq(
    neo4jInternalName := "ACOMPANY",
    neo4jInternalOrgs := Seq("a.company"),
    neo4jTagsLabels   := Map("client" -> "Service_Client", "web" -> "Web")
  )

  val buildSettings = Defaults.defaultSettings ++ Seq(
    ...
  ) ++ neoSettings
}
```

Note that if your build.scala contains multiple projects the previous snippet to apply to each of the subproject. 
Because of the way that the plugin loads the dependencies into the graph database though, 
I will suggest you to run the plugin for multiple project definition not from the root project, 
but by selecting the project and call the task, rather than calling the task on the root project. 

For example, for the [full Build.scala]({{ site.url }}/versions) definition of the svc-user example, I load the 
dependency of core, client and server as follow: 

``````

Look up the section **Settings** for more information about what each of this setting represent.

<h2 id="tasks">Tasks</h2>

Installing the plugin will add to your project 3 new tasks, **neo4jShowDependencies** , **neo4jWriteDependencies** and **neo4jLoadDependencies** . By the way, you can see all the 
tasks available in the sbt console by running 

```sbt> tasks -V```

<img src="{{ site.url }}/assets/img/taskv.png" width="600">

#### neo4jShowDependencies
This task will print on screen the cypher statements that would be used to create the nodes (projects) and relations (dependencies) in the neo4j database. 
This task is expecially useful for development, but also is a good way to explore how the graph would be generated for a particular project.  

#### neo4jWriteDependencies
This task will print on file the cypher statements that would be used to create the nodes (projects) and relations (dependencies) in the neo4j database. 
Calling this task just create the file, the location of which is defined in the plugin itself as:

```neo4jCypherScript := (baseDirectory in Compile).value / "neodependencies" / "nodesandrelations.cyp"```

Also note that the task to load the dependencies **dependsOn** this task. 

#### neo4jLoadDependencies
This is the task that creates the nodes (projects) and relations (dependencies) in the neo4j database. 
All it does is calling the neo4jWriteDependencies and than passing that file to the neo4j console. Because of that it assumes that 

1. Your neo4j server is running
2. You have set a variable **NEO4J_HOME** in your environment

If any of these requirements is not satisfied you will get a message of error and you won't be able to load your dependencies in the graph. 

<h2 id="settings">Settings</h2>