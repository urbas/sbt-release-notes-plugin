import sbt._
import si.urbas.sbt.releasenotes._

object BuildConfiguration extends Build {

  val root = project.in(file("."))
    .enablePlugins(MdReleaseNotesPlugin)
    .settings(Seq(
    TaskKey[Unit]("check") := {
      val releaseNotesFile = ReleaseNotesPlugin.autoImport.releaseNotesFile.value
      assertFilesHaveSameContents("release notes", file("expectedReleaseNotes"), releaseNotesFile)
    },
    TaskKey[Unit]("checkBlessedReleaseNotes") := {
      val blessedReleaseNotesBodyFile: File = ReleaseNotesPlugin.autoImport.releaseNotesPreviousVersionBodyFile.value
      assertFilesHaveSameContents("blessed release notes file", file("expectedBlessedReleaseNotes"), blessedReleaseNotesBodyFile)
    }): _*)

  private def assertFilesHaveSameContents(fileDescription: String, expectedFile: File, actualFile: File) {
    if (!actualFile.exists()) {
      sys.error(s"$fileDescription does not exist.")
    }
    val expectedContents = IO.read(expectedFile)
    val actualContents = IO.read(actualFile)
    if (expectedContents != actualContents) {
      sys.error(s"$fileDescription does not contain the expected contents.\n\nActual contents:\n\n[$actualContents]\n\nExpected contents:\n\n[$expectedContents]\n\n")
    }
  }
}