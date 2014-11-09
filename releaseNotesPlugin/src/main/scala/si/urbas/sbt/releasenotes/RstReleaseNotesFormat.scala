package si.urbas.sbt.releasenotes

import sbt.Keys._
import sbt._

object RstReleaseNotesFormat extends ReleaseNotesFormat(
  header = Def.value("Release notes\n=============\n\n"),
  versionHeader = version { v => s"$v\n${v.map(_ => "-").mkString}\n\n" },
  releaseNotesFileName = Def.value("RELEASE_NOTES.rst"),
  releaseNotesEntriesIncludeFilter = "*.rst"
)