package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._

object RootFolderReleaseNotesStrategy extends ReleaseNotesStrategy {

  import si.urbas.sbt.releasenotes.ReleaseNotesPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(releaseNotesBlessedFile := Some(baseDirectory.value / releaseNotesFileName.value))
  }
}
