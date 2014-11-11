package si.urbas.sbt.releasenotes.strategies

import sbt.Def
import si.urbas.sbt.content.{GroupedByFirstLineContent, CompoundContent, TimestampedContent}
import si.urbas.sbt.releasenotes.ReleaseNotesPlugin._
import si.urbas.sbt.releasenotes.ReleaseNotesStrategy

object GroupReleaseNotesByFirstLine extends ReleaseNotesStrategy {

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      releaseNotesCurrentVersionBody := {
        releaseNotesCurrentVersionBody.value match {
          case compoundContent: CompoundContent =>
            GroupedByFirstLineContent(compoundContent)
          case content =>
            content
        }
      }
    )
  }
}

