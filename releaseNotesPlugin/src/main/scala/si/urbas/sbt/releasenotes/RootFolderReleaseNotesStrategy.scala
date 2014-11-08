package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._

object RootFolderReleaseNotesStrategy extends AutoPlugin {

  override def requires: Plugins = ReleaseNotesPlugin

  import si.urbas.sbt.releasenotes.ReleaseNotesPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(releaseNotesDir := baseDirectory.value)
  }
}
