sbtPlugin := true

name := "sbt-neo-dependencies"

organization := "com.joypeg"

publishMavenStyle := false

crossBuildingSettings

CrossBuilding.crossSbtVersions := Seq("0.12", "0.13")

unmanagedSourceDirectories in Compile <+= (sourceDirectory in Compile, sbtVersion in sbtPlugin) {
  (s,v) => s / ("scala-sbt-"+v)
}
