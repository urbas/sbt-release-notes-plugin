package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._
import si.urbas.sbt.releasenotes.ReleaseNotesFormat._

class ReleaseNotesFormat(header: Def.Initialize[String] = DEFAULT_HEADER,
                         versionHeader: Def.Initialize[String] = DEFAULT_VERSION_HEADER,
                         footer: Def.Initialize[String] = EMPTY_FOOTER,
                         releaseNotesFileName: Def.Initialize[String] = RELEASE_NOTES_FILE_NAME,
                         releaseNotesEntriesIncludeFilter: FileFilter) extends AutoPlugin {

  override def requires: Plugins = ReleaseNotesPlugin

  import si.urbas.sbt.releasenotes.ReleaseNotesPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      includeFilter.in(releaseNotes) := releaseNotesEntriesIncludeFilter,
      releaseNoteHeader := header.value,
      releaseNoteFooter := footer.value,
      releaseNoteVersionHeader := versionHeader.value,
      releaseNotesFile := releaseNotesDir.value / releaseNotesFileName.value
    )
  }
}

object ReleaseNotesFormat {
  val EMPTY_FOOTER = Def.value("")
  val DEFAULT_HEADER = Def.value("Release notes\n\n\n")
  val DEFAULT_VERSION_HEADER = version { version => s"$version\n\n"}
  val RELEASE_NOTES_FILE_NAME = Def.value("RELEASE_NOTES")
}
