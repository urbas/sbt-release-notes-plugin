package si.urbas.sbt.releasenotes.strategies

import sbt.Keys._
import sbt._
import si.urbas.sbt.releasenotes.ReleaseNotesPlugin._
import si.urbas.sbt.releasenotes.ReleaseNotesStrategy
import si.urbas.sbt.releasenotes.formats.RstReleaseNotesFormat

object SphinxReleaseNotesStrategy extends ReleaseNotesStrategy {

  override def requires: Plugins = super.requires && RstReleaseNotesFormat

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      releaseNotesFileName := "releaseNotes.rst",
      releaseNotesDir := sourceDirectory.value / "sphinx",
      releaseNotesBlessedFile := None,
      cleanFiles += releaseNotesFile.value
    )
  }
}
