organization := "si.urbas"

name := "sbt-release-notes-docs"

version := "0.0.2-SNAPSHOT"

val docs = project.in(file("."))
  .enablePlugins(SphinxReleaseNotesStrategy)

site.settings

site.sphinxSupport()
