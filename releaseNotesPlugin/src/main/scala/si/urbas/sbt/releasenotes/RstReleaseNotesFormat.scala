package si.urbas.sbt.releasenotes

import sbt.Keys._
import si.urbas.sbt.content._

object RstReleaseNotesFormat extends ReleaseNotesFormat(
  header = toContentDef("Release notes\n=============\n\n"),
  versionHeader = toContentDef(version { v => s"$v\n${v.map(_ => "-").mkString}\n\n"}),
  overriddenReleaseNotesFileName = "RELEASE_NOTES.rst"
)