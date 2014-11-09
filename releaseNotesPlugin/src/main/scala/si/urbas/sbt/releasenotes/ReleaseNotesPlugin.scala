package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._
import si.urbas.sbt.content._
import si.urbas.sbt.util._

object ReleaseNotesPlugin extends AutoPlugin {

  val RELEASE_NOTES_DIR_NAME = "releasenotes"
  val RELEASE_NOTES_PREVIOUS_VERSION_BODY_FILE_NAME = "releaseNotesBody.previousVersion"
  val DEFAULT_RELEASE_NOTES_FILE = "RELEASE_NOTES"


  object autoImport {
    val releaseNotes = taskKey[Unit]("concatenates the release notes entries into a single file and puts the resulting release notes file into a target folder. The resulting release notes file is made for the current version.")
    val blessReleaseNotes = taskKey[Unit]("prepares the release notes for the next version. You must commit the changes this task makes to the VCS.")

    val releaseNotesSourceDir = sourceDirectory.in(releaseNotes)
    val releaseNotesSources = sources.in(releaseNotes)
    val releaseNotesDir = target.in(releaseNotes)
    val releaseNotesPreviousVersionBodyFile = settingKey[File]("the file that contains the body of the release notes for the previous version. Body files do not contain the top header and footer.").in(releaseNotes)
    val releaseNotesFileName = settingKey[String]("the name of final release notes file for the current version. This file can be used in the documentation.").in(releaseNotes)
    val releaseNotesFile = settingKey[File]("the full location of the final release notes file for the current version. This file can be used in the documentation.").in(releaseNotes)
    val releaseNotesBlessedFile = settingKey[Option[File]]("the final release notes file for the current version that will be committed to the VCS.").in(releaseNotes)

    val releaseNotesHeader = taskKey[TimestampedContent]("the header that will be prepended at the top of the release notes file.").in(releaseNotes)
    val releaseNotesFooter = taskKey[TimestampedContent]("the footer that will be appended at the bottom of the release notes file.").in(releaseNotes)
    val releaseNotesVersionHeader = taskKey[TimestampedContent]("the header that will be prepended above the release notes for the current version but below the top header.").in(releaseNotes)
    val releaseNotesCurrentVersionBody = taskKey[TimestampedContent]("returns the content of release notes. By default, this contains only the concatenated release note entries (without the per-version header).").in(releaseNotes)
    val releaseNotesPreviousVersion = taskKey[TimestampedContent]("returns only the previous release notes (without the top header or footer).").in(releaseNotes)
    val releaseNotesBody = taskKey[TimestampedContent]("returns only the body of the release notes for the current version. The body contains the concatenated release notes entries but not the top header and footer.").in(releaseNotes)
  }

  import si.urbas.sbt.releasenotes.ReleaseNotesPlugin.autoImport._

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
        releaseNotesPreviousVersion.value.map(c => if (c.isEmpty) "" else s"\n\n$c")
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
