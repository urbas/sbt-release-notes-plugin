organization := "si.urbas"

name := "sbt-release-notes-github-sample"

version := "0.0.2"

val root = project.in(file("."))
  .enablePlugins(MdReleaseNotesFormat, RootFolderReleaseNotesStrategy)