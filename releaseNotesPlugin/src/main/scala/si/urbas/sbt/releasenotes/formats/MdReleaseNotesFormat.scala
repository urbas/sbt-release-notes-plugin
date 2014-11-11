package si.urbas.sbt.releasenotes.formats

import sbt.Keys._
import si.urbas.sbt.content._
import si.urbas.sbt.releasenotes.ReleaseNotesFormat

object MdReleaseNotesFormat extends ReleaseNotesFormat(
  header = toContentDef("# Release notes\n\n"),
  versionHeader = toContentDef(version { v => s"## $v\n\n" }),
  overriddenReleaseNotesFileName = "RELEASE_NOTES.md"
)
