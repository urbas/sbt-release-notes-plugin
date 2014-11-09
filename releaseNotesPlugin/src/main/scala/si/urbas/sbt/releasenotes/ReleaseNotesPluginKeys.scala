package si.urbas.sbt.releasenotes
import sbt.Keys._
import sbt._
import si.urbas.sbt.content.TimestampedContent

trait ReleaseNotesPluginKeys {
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
