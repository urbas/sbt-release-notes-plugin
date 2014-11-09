organization := "si.urbas"

name := "sbt-release-notes-github-sample"

version := IO.read(file("../LATEST_RELEASE_VERSION")).trim

val root = project.in(file("."))
  .enablePlugins(RstReleaseNotesFormat)

site.settings

site.sphinxSupport()
