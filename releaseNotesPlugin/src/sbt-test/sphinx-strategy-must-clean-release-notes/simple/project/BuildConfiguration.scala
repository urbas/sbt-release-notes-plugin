import sbt._
import si.urbas.sbt.releasenotes._
import si.urbas.sbt.releasenotes.strategies.SphinxReleaseNotesStrategy

object BuildConfiguration extends Build {
  val root = project.in(file("."))
    .enablePlugins(SphinxReleaseNotesStrategy)
}