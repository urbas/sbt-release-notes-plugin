import sbt._
import si.urbas.sbt.releasenotes.formats._
import si.urbas.sbt.releasenotes.strategies._
import si.urbas.sbt.releasenotes.test._

object BuildConfiguration extends Build {
  val root = project.in(file("."))
    .enablePlugins(MdReleaseNotesFormat, RootFolderReleaseNotesStrategy, HeaderlessReleaseNotesStrategy, ReleaseNotesSelfTestPlugin)
}