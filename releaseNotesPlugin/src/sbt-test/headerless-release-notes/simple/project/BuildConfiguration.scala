import sbt._
import si.urbas.sbt.releasenotes._
import si.urbas.sbt.releasenotes.formats.MdReleaseNotesFormat
import si.urbas.sbt.releasenotes.strategies.{HeaderlessReleaseNotesStrategy, RootFolderReleaseNotesStrategy}
import si.urbas.sbt.releasenotes.test._

object BuildConfiguration extends Build {
  val root = project.in(file("."))
    .enablePlugins(MdReleaseNotesFormat, RootFolderReleaseNotesStrategy, HeaderlessReleaseNotesStrategy, ReleaseNotesSelfTestPlugin)
}