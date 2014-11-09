package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._
import si.urbas.sbt.content._

object MdReleaseNotesFormat extends ReleaseNotesFormat(
  header = toContentDef("# Release notes\n\n"),
  versionHeader = toContentDef(version { v => s"## $v\n\n" }),
  overriddenReleaseNotesFileName = "RELEASE_NOTES.md"
)
