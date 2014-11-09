import sbt._
import si.urbas.sbt.releasenotes.{RootFolderReleaseNotesStrategy, MdReleaseNotesFormat}
import si.urbas.sbt.releasenotes.test.ReleaseNotesSelfTestPlugin

object BuildConfiguration extends Build {
  val root = project.in(file("."))
    .enablePlugins(MdReleaseNotesFormat, RootFolderReleaseNotesStrategy, ReleaseNotesSelfTestPlugin)
}