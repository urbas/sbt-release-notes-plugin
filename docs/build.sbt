organization := "si.urbas"

name := "sbt-release-notes-docs"

version := IO.read(file("../LATEST_RELEASE_VERSION")).trim

val docs = project.in(file("."))
  .enablePlugins(SphinxReleaseNotesStrategy)

site.settings

site.sphinxSupport()
