package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._

case class PlainReleaseNotesFormat(header: Def.Initialize[String]  = PlainReleaseNotesFormat.defaultHeader,
                                   perVersionHeader: Def.Initialize[String] = PlainReleaseNotesFormat.defaultPerVersionHeader,
                                   footer: Def.Initialize[String] = Def.value("")) extends AutoPlugin {

  override def requires: Plugins = ReleaseNotesPlugin

  import si.urbas.sbt.releasenotes.ReleaseNotesPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      includeFilter.in(releaseNotes) := "*",
      releaseNoteHeader := header.value,
      releaseNoteFooter := footer.value,
      releaseNoteVersionHeader := perVersionHeader.value,
      releaseNotesFile := releaseNotesDir.value / "RELEASE_NOTES"
    )
  }
}

object PlainReleaseNotesFormat {

  def defaultHeader: Def.Initialize[String] = {
    Def.value("Release notes\n\n\n")
  }

  def defaultPerVersionHeader: Def.Initialize[String] = {
    version {
      version =>
        s"$version\n\n"
    }
  }

}
