import sbt.Keys._
import sbt.ScriptedPlugin._
import sbt._
import xerial.sbt.Sonatype.SonatypeKeys._
import xerial.sbt.Sonatype.sonatypeSettings
import sbtrelease.ReleasePlugin._

object BuildConfiguration extends Build {

  val projectName = "sbt-release-notes-plugin"
  val projectUrl = s"https://github.com/urbas/$projectName"
  val projectScmUrl = s"git@github.com:urbas/$projectName.git"
  val ownerName = "urbas"
  val ownerUrl = "https://github.com/urbas"
  val credentialsFile = Path.userHome / ".ivy2" / ".credentials"

  override def settings: Seq[Def.Setting[_]] = {
    super.settings ++ Seq(
      organization := "si.urbas",
      pomExtra := pomExtraSettings,
      // NOTE: We have to package documentation to conform to Sonatype's Repo policy
      publishArtifact in(Compile, packageDoc) := true,
      publishArtifact in(Compile, packageSrc) := true,
      publishArtifact in(Test, packageSrc) := false,
      publishArtifact in(Test, packageDoc) := false,
      credentials ++= {
        if (credentialsFile.exists()) Seq(Credentials(credentialsFile)) else Nil
      },
      publishMavenStyle := true,
      profileName := "org.xerial",
      publishArtifact.in(Compile, packageDoc) := false
    )
  }

  lazy val root = project.in(file("."))
    .aggregate(releaseNotesPlugin)
    .settings(publish := {})
    .settings(publishLocal := {})

  lazy val releaseNotesPlugin = project.in(file("releaseNotesPlugin"))
    .settings(scriptedSettings ++ sonatypeSettings ++ releaseSettings: _*)
    .settings(
      name := "sbt-release-notes-plugin",
      sbtPlugin := true,
      scriptedLaunchOpts ++= Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + version.value),
      scriptedBufferLog := false
    )

  private lazy val pomExtraSettings = {
    <url>
      {projectUrl}
    </url>
      <licenses>
        <license>
          <name>Apache 2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0.txt</url>
        </license>
      </licenses>
      <scm>
        <connection>
          scm:git:
          {projectScmUrl}
        </connection>
        <developerConnection>
          scm:git:
          {projectScmUrl}
        </developerConnection>
        <url>
          {projectUrl}
        </url>
      </scm>
      <developers>
        <developer>
          <id>
            {ownerName}
          </id>
          <name>
            {ownerName}
          </name>
          <url>
            {ownerUrl}
          </url>
        </developer>
      </developers>
  }

}