package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._
import si.urbas.sbt.content._
import si.urbas.sbt.util._

object ReleaseNotesPlugin extends AutoPlugin with ReleaseNotesPluginKeys {

  val RELEASE_NOTES_DIR_NAME = "releasenotes"
  val RELEASE_NOTES_PREVIOUS_VERSION_BODY_FILE_NAME = "releaseNotesBody.previousVersion"
  val DEFAULT_RELEASE_NOTES_FILE = "RELEASE_NOTES"

  object autoImport extends ReleaseNotesPluginKeys

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      releaseNotesSourceDir := sourceDirectory.value / RELEASE_NOTES_DIR_NAME,
      sourceDirectories.in(releaseNotes) <<= releaseNotesSourceDir { dir => Seq(dir)},
      excludeFilter.in(releaseNotes) := new SimpleFileFilter(_.equals(releaseNotesPreviousVersionBodyFile.value)),
      releaseNotesSources <<= Defaults.collectFiles(sourceDirectories.in(releaseNotes), includeFilter.in(releaseNotes), excludeFilter.in(releaseNotes)),
      releaseNotesDir := target.value / RELEASE_NOTES_DIR_NAME,
      releaseNotesPreviousVersionBodyFile := releaseNotesSourceDir.value / RELEASE_NOTES_PREVIOUS_VERSION_BODY_FILE_NAME,
      releaseNotesFileName := DEFAULT_RELEASE_NOTES_FILE,
      releaseNotesFile := releaseNotesDir.value / releaseNotesFileName.value,
      releaseNotesCurrentVersionBody := toContent(releaseNotesSources.value, "\n\n"),
      releaseNotesPreviousVersion <<= previousVersionBodyTask(),
      releaseNotesBody <<= currentVersionBodyTask(),
      releaseNotes <<= releaseNotesTask(),
      blessReleaseNotes <<= blessReleaseNotesTask(),
      releaseNotesBlessedFile := None,
      watchSources ++= releaseNotesSources.value :+ releaseNotesPreviousVersionBodyFile.value
    )
  }

  private def currentVersionBodyTask(): Def.Initialize[Task[TimestampedContent]] = {
    Def.task[TimestampedContent] {
      releaseNotesVersionHeader.value +
        releaseNotesCurrentVersionBody.value +
        releaseNotesPreviousVersion.value.transform(c => if (c.isEmpty) "" else s"\n\n$c")
    }
  }

  private def releaseNotesTask(): Def.Initialize[Task[Unit]] = {
    Def.task[Unit] {
      overwriteIfOlder(releaseNotesFile.value, releaseNotesHeader.value + releaseNotesBody.value + releaseNotesFooter.value)
    }
  }

  private def blessReleaseNotesTask(): Def.Initialize[Task[Unit]] = {
    (releaseNotes, releaseNotesFile, releaseNotesPreviousVersionBodyFile, releaseNotesBlessedFile, releaseNotesBody, releaseNotesSources).map {
      (releaseNotes, releaseNotesFile, releaseNotesPreviousVersionBodyFile, releaseNotesBlessedFile, releaseNotesBody, releaseNotesSources) =>
        overwrite(releaseNotesPreviousVersionBodyFile, releaseNotesBody)
        releaseNotesBlessedFile.map {
          blessedFile =>
            ifSourceNewer(releaseNotesFile, blessedFile)(IO.copyFile(_, _))
        }
        IO.delete(releaseNotesSources)
    }
  }

  private def previousVersionBodyTask(): Def.Initialize[Task[TimestampedContent]] = {
    Def.task[TimestampedContent] {
      FileContent(releaseNotesPreviousVersionBodyFile.value)
    }
  }
}
