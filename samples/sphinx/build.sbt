organization := "si.urbas"

name := "sbt-release-notes-docs"

version := "0.0.2-SNAPSHOT"

val sphinx = project.in(file("."))
  .enablePlugins(SphinxReleaseNotesStrategy)

site.settings

site.sphinxSupport()

import com.typesafe.sbt.site.SphinxSupport._

generate.in(Sphinx) <<= generate.in(Sphinx).dependsOn(releaseNotes)