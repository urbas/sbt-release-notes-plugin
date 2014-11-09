package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._

object ReleaseNotesPlugin extends AutoPlugin {

  val RELEASE_NOTES_DIR_NAME = "releasenotes"
  val RELEASE_NOTES_BODY_CURRENT_VERSION_FILE_NAME = "releaseNotesBody"
  val RELEASE_NOTES_BODY_PREVIOUS_VERSION_FILE_NAME = "releaseNotesBody.previousVersion"

  object autoImport {
    val releaseNotes = taskKey[Unit]("concatenates the release notes entries into a single file and puts the resulting release notes file into a target folder. The resulting release notes file is made for the current version.")
    val blessReleaseNotes = taskKey[Unit]("prepares the release notes for the next version. You can commit the changes to your VCS.")

    val releaseNotesSourceDir = sourceDirectory.in(releaseNotes)
    val releaseNotesSources = sources.in(releaseNotes)
    val releaseNotesDir = target.in(releaseNotes)
    val releaseNotesPreviousVersionBodyFile = settingKey[File]("the file that contains the body of the release notes for the previous version.").in(releaseNotes)
    val releaseNotesFile = settingKey[File]("the accumulated release notes file for the current version. This file can be used in the documentation.").in(releaseNotes)

    val releaseNotesHeader = taskKey[String]("the header that will be prepended at the top of the release notes file.").in(releaseNotes)
    val releaseNotesFooter = taskKey[String]("the footer that will be appended at the bottom of the release notes file.").in(releaseNotes)
    val releaseNotesVersionHeader = taskKey[String]("the header that will be prepended above the release notes for the current version but below the top header.").in(releaseNotes)
    val releaseNotesCurrentVersionEntries = taskKey[String]("returns the concatenated release note entries.").in(releaseNotes)
    val releaseNotesPreviousVersionBody = taskKey[String]("returns only the body of the previous release notes.").in(releaseNotes)
    val releaseNotesBody = taskKey[String]("returns only the body of the release notes for the current version. The body contains the concatenated release notes entries but not the top header and footer.").in(releaseNotes)
  }

  import si.urbas.sbt.releasenotes.ReleaseNotesPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      releaseNotesSourceDir := sourceDirectory.value / RELEASE_NOTES_DIR_NAME,
      sourceDirectories.in(releaseNotes) <<= releaseNotesSourceDir { dir => Seq(dir)},
      excludeFilter.in(releaseNotes) := new SimpleFileFilter(_.equals(releaseNotesPreviousVersionBodyFile.value)),
      releaseNotesSources <<= Defaults.collectFiles(sourceDirectories.in(releaseNotes), includeFilter.in(releaseNotes), excludeFilter.in(releaseNotes)),
      releaseNotesDir := target.value / RELEASE_NOTES_DIR_NAME,
      releaseNotesCurrentVersionEntries := releaseNotesSources.value.sortBy(_.getName).map(IO.read(_)).mkString("\n\n"),
      releaseNotesPreviousVersionBodyFile := releaseNotesSourceDir.value / RELEASE_NOTES_BODY_PREVIOUS_VERSION_FILE_NAME,
      releaseNotesPreviousVersionBody <<= previousVersionBodyTask(),
      releaseNotesBody <<= currentVersionBodyTask(),
      releaseNotes <<= releaseNotesTask(),
      blessReleaseNotes := IO.write(releaseNotesPreviousVersionBodyFile.value, releaseNotesBody.value)
    )
  }

  private def releaseNotesTask(): Def.Initialize[Task[Unit]] = {
    Def.task[Unit] {
      val releaseNotesContent = Seq(releaseNotesHeader.value, releaseNotesBody.value, releaseNotesFooter.value).mkString
      IO.write(releaseNotesFile.value, releaseNotesContent)
    }
  }

  private def currentVersionBodyTask(): Def.Initialize[Task[String]] = {
    Def.task[String] {
      val previousVersionBody = releaseNotesPreviousVersionBody.value
      Seq(releaseNotesVersionHeader.value, releaseNotesCurrentVersionEntries.value, if (previousVersionBody.isEmpty) "" else "\n\n", previousVersionBody)
        .mkString
    }
  }

  private def previousVersionBodyTask(): Def.Initialize[Task[String]] = {
    Def.task[String] {
      val prevVersionBodyFile = releaseNotesPreviousVersionBodyFile.value
      if (prevVersionBodyFile.exists()) {
        IO.read(prevVersionBodyFile)
      }
      else {
        ""
      }
    }
  }
}
