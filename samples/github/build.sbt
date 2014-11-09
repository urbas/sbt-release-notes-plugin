organization := "si.urbas"

name := "sbt-release-notes-github-sample"

version := "0.0.1"

val root = project.in(file("."))
  .enablePlugins(MdReleaseNotesFormat, RootFolderReleaseNotesStrategy)