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
and files ending in .scala, located in the project/ subdirectory the base directory.

<h4 id="buildsbt">build.sbt</h4>

<h4 id="buildscala">build.scala</h4>