package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._
import si.urbas.sbt.releasenotes.ReleaseNotesFormat._
import si.urbas.sbt.content._

class ReleaseNotesFormat(header: Def.Initialize[TimestampedContent] = DEFAULT_HEADER,
                         versionHeader: Def.Initialize[TimestampedContent] = DEFAULT_VERSION_HEADER,
                         footer: Def.Initialize[TimestampedContent] = EMPTY_FOOTER,
                         releaseNotesFileName: Def.Initialize[String] = RELEASE_NOTES_FILE_NAME) extends AutoPlugin {

  override def requires: Plugins = ReleaseNotesPlugin

  import si.urbas.sbt.releasenotes.ReleaseNotesPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      includeFilter.in(releaseNotes) := RELEASE_NOTES_ENTRIES_INCLUDE_FILTER,
      releaseNotesHeader := header.value,
      releaseNotesFooter := footer.value,
      releaseNotesVersionHeader := versionHeader.value,
      releaseNotesFile := releaseNotesDir.value / releaseNotesFileName.value
    )
  }
}

object ReleaseNotesFormat {
  val RELEASE_NOTES_ENTRIES_INCLUDE_FILTER = -DirectoryFilter
  val EMPTY_FOOTER = toContentDef("")
  val DEFAULT_HEADER = toContentDef("Release notes\n\n\n")
  val DEFAULT_VERSION_HEADER = toContentDef(version { version => s"$version\n\n" })
  val RELEASE_NOTES_FILE_NAME = Def.value("RELEASE_NOTES")
}
