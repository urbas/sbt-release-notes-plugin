package si.urbas.sbt.releasenotes.strategies

import sbt._
import si.urbas.sbt.releasenotes.ReleaseNotesPlugin._
import si.urbas.sbt.releasenotes.ReleaseNotesStrategy
import si.urbas.sbt.releasenotes.formats.MdReleaseNotesFormat

object GitHubReleaseNotesStrategy extends ReleaseNotesStrategy {

  override def requires: Plugins = {
    super.requires && MdReleaseNotesFormat && HeaderlessReleaseNotesStrategy && RootFolderReleaseNotesStrategy
  }

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      releaseNotesPreviousVersionBodyFile := {
        releaseNotesBlessedFile.value.getOrElse(throw new IllegalArgumentException("The GitHub release notes strategy requires the blessed release notes file."))
      }
    )
  }
}

