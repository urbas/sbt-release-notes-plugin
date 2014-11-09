package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._

object SphinxReleaseNotesStrategy extends ReleaseNotesStrategy {

  import si.urbas.sbt.releasenotes.ReleaseNotesPlugin.autoImport._

  override def requires: Plugins = super.requires && RstReleaseNotesFormat

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      releaseNotesFileName := "releaseNotes.rst",
      releaseNotesDir := sourceDirectory.value / "sphinx",
      releaseNotesBlessedFile := None,
      cleanFiles <+= releaseNotesFile
    )
  }
}
