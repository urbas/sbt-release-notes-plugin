package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._

object MdReleaseNotesFormat extends ReleaseNotesFormat(
  header = Def.value("# Release notes\n\n"),
  versionHeader = version { v => s"## $v\n\n" },
  releaseNotesFileName = Def.value("RELEASE_NOTES.md"),
  releaseNotesEntriesIncludeFilter = "*.md"
)
