sbtPlugin := true

name := "sbt-neo-dependencies"

organization := "com.joypeg"

publishMavenStyle := false

crossBuildingSettings

CrossBuilding.crossSbtVersions := Seq("0.12", "0.13")

sbtVersion in sbtPlugin := "0.13"
