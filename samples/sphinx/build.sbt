import com.typesafe.sbt.site.SphinxSupport

organization := "si.urbas"

name := "sbt-release-notes-docs"

version := "0.0.2-SNAPSHOT"

val sphinx = project.in(file("."))
  .enablePlugins(SphinxReleaseNotesStrategy)

site.settings

site.sphinxSupport()

SphinxSupport.generate.in(SphinxSupport.Sphinx) <<= SphinxSupport.generate.in(SphinxSupport.Sphinx).dependsOn(ReleaseNotesPlugin.autoImport.releaseNotes)