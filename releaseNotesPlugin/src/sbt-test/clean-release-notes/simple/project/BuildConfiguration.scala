import sbt._
import si.urbas.sbt.releasenotes._

object BuildConfiguration extends Build {
  val root = project.in(file("."))
    .enablePlugins(RstReleaseNotesFormat)
}