package si.urbas.sbt.releasenotes

import java.net.URL
import java.nio.charset.Charset
import javax.net.ssl.HttpsURLConnection

import sbt.Keys._
import sbt._
import si.urbas.sbt.releasenotes.ReleaseNotesPlugin._

object GitHubReleasesPlugin extends AutoPlugin {
  override def requires: Plugins = super.requires && ReleaseNotesPlugin

  object autoImport {
    val createGitHubRelease = taskKey[Unit]("creates a draft release for the current version.")
    val gitHubUserName = settingKey[String]("the username for communication with the GitHub API.").in(createGitHubRelease)
    val gitHubRepositoryName = settingKey[String]("the name of your repository for which to create release notes.").in(createGitHubRelease)
    val gitHubReleaseTag = settingKey[String]("the git tag to use for the release.").in(createGitHubRelease)
    val gitHubReleaseTitle = settingKey[String]("the title of the release.").in(createGitHubRelease)
    val gitHubReleaseNotesBody = TaskKey[String]("the release notes to use for the release.").in(createGitHubRelease)
    val gitHubReleaseNotesTokenFile = settingKey[File]("the file that contains the token with which the release notes plugin can connect to the GitHub API.").in(createGitHubRelease)
  }

  import si.urbas.sbt.releasenotes.GitHubReleasesPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = {
    super.projectSettings ++ Seq(
      createGitHubRelease <<= createGitHubReleaseTask(),
      gitHubReleaseNotesTokenFile := file(System.getProperty("user.home")) / ".sbt" / ".github-release-notes-token",
      gitHubReleaseNotesBody := releaseNotesCurrentVersionBody.value.content.replace("\"", "\\\"").replace("\n", "\\n"),
      gitHubReleaseTag := s"v${version.value}",
      gitHubReleaseTitle := s"v${version.value}"
    )
  }

  private def createGitHubReleaseTask(): Def.Initialize[Task[Unit]] = {
    Def.task[Unit] {
      streams.value.log.info("Creating a GitHub release...")
      val gitHubApiToken = IO.read(gitHubReleaseNotesTokenFile.value).trim
      val gitHubReleasesApiUrl = new URL(s"https://api.github.com/repos/${gitHubUserName.value}/${gitHubRepositoryName.value}/releases")
      val gitHubReleasesApiConnection = gitHubReleasesApiUrl.openConnection().asInstanceOf[HttpsURLConnection]
      try {
        gitHubReleasesApiConnection.setDoOutput(true)
        gitHubReleasesApiConnection.setDoInput(true)
        gitHubReleasesApiConnection.setRequestMethod("POST")
        gitHubReleasesApiConnection.setRequestProperty("Content-Type", "application/json")
        gitHubReleasesApiConnection.setRequestProperty("Authorization", s"token $gitHubApiToken")
        val output = gitHubReleasesApiConnection.getOutputStream
        try {
          val requestData = s"""{"tag_name": "${gitHubReleaseTag.value}", "name": "${gitHubReleaseTitle.value}", "body": "${gitHubReleaseNotesBody.value}", "draft": true}"""
          output.write(requestData.getBytes(Charset.forName("utf-8")))
        } finally {
          output.close()
        }
        streams.value.log.info("GitHub release created. Details: " + IO.readStream(gitHubReleasesApiConnection.getInputStream))
      } finally {
        gitHubReleasesApiConnection.disconnect()
      }
    }
  }
}
