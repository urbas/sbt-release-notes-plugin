package si.urbas.sbt.releasenotes.strategies

import sbt.Def
import si.urbas.sbt.content.StringContent
import si.urbas.sbt.releasenotes.ReleaseNotesPlugin._
import si.urbas.sbt.releasenotes.ReleaseNotesStrategy

object HeaderlessReleaseNotesStrategy extends ReleaseNotesStrategy {

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      releaseNotesHeader := StringContent(""),
      releaseNotesFooter := StringContent(""),
      releaseNotesPreviousVersionBodyFile := releaseNotesBlessedFile.value.getOrElse(releaseNotesPreviousVersionBodyFile.value)
    )
  }
}

