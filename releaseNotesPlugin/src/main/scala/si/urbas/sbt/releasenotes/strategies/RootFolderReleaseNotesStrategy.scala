package si.urbas.sbt.releasenotes.strategies

import sbt.Keys._
import sbt._
import si.urbas.sbt.releasenotes.ReleaseNotesStrategy

object RootFolderReleaseNotesStrategy extends ReleaseNotesStrategy {

  import si.urbas.sbt.releasenotes.ReleaseNotesPlugin._

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(releaseNotesBlessedFile := Some(baseDirectory.value / releaseNotesFileName.value))
  }
}
