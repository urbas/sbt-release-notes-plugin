package si.urbas.sbt.releasenotes.test

import sbt._
import si.urbas.sbt.releasenotes.{ReleaseNotesStrategy, ReleaseNotesPlugin}

object ReleaseNotesSelfTestPlugin extends ReleaseNotesStrategy {

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      TaskKey[Unit]("assertReleaseNotes") := {
        val releaseNotesFile = ReleaseNotesPlugin.autoImport.releaseNotesFile.value
        assertFilesHaveSameContents("release notes", file("expectedReleaseNotes"), releaseNotesFile)
      },
      TaskKey[Unit]("assertPreviousReleaseNotes") := {
        val previousReleaseNotesBodyFile = ReleaseNotesPlugin.autoImport.releaseNotesPreviousVersionBodyFile.value
        assertFilesHaveSameContents("blessed release notes file", file("expectedReleaseNotesPreviousVersionBody"), previousReleaseNotesBodyFile)
      }
    )
  }

  private def assertFilesHaveSameContents(fileDescription: String, expectedFile: File, actualFile: File) {
    if (!actualFile.exists()) {
      sys.error(s"$fileDescription does not exist.")
    }
    val expectedContents = IO.read(expectedFile)
    val actualContents = IO.read(actualFile)
    if (expectedContents != actualContents) {
      sys.error(s"The `$fileDescription` file does not contain the expected contents.\n\nActual contents:\n\n[$actualContents]\n\nExpected contents:\n\n[$expectedContents]\n\n")
    }
  }

}
