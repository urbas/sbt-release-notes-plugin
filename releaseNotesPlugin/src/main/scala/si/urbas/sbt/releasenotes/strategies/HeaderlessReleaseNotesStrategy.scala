package si.urbas.sbt.releasenotes.strategies

import sbt.Def
import si.urbas.sbt.content.NoContent
import si.urbas.sbt.releasenotes.ReleaseNotesPlugin._
import si.urbas.sbt.releasenotes.ReleaseNotesStrategy

object HeaderlessReleaseNotesStrategy extends ReleaseNotesStrategy {

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      releaseNotesHeader := NoContent,
      releaseNotesFooter := NoContent
    )
  }
}

