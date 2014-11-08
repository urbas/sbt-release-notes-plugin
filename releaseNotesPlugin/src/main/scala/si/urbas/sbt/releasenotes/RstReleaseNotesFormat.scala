package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._

object RstReleaseNotesFormat extends AutoPlugin {

  override def requires: Plugins = ReleaseNotesPlugin

  import si.urbas.sbt.releasenotes.ReleaseNotesPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      includeFilter.in(releaseNotes) := "*.rst",
      releaseNoteHeader := "Release notes\n=============\n\n",
      releaseNoteFooter := "",
      releaseNoteVersionHeader := s"## ${version.value}\n${version.value.map(_ => "-").mkString}\n\n",
      releaseNotesFile := releaseNotesDir.value / "RELEASE_NOTES.rst"
    )
  }
}
