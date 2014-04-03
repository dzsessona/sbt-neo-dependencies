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
