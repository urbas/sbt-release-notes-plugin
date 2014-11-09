import sbt.Keys._
import sbt.ScriptedPlugin._
import sbt._
import sbtrelease.ReleasePlugin.ReleaseKeys._
import sbtrelease.ReleasePlugin._
import sbtrelease.ReleaseStateTransformations._
import sbtrelease._
import si.urbas.sbt.releasenotes.ReleaseNotesPlugin._
import si.urbas.sbt.releasenotes._
import xerial.sbt.Sonatype.SonatypeKeys._
import xerial.sbt.Sonatype.sonatypeSettings

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
      sources.in(Compile, doc) := Nil,
      credentials ++= {
        if (credentialsFile.exists()) Seq(Credentials(credentialsFile)) else Nil
      },
      publishMavenStyle := true,
      profileName := "org.xerial"
    )
  }

  lazy val root = project.in(file("."))
    .aggregate(releaseNotesPlugin)
    .settings(publish := {})
    .settings(publishLocal := {})
    .settings(sonatypeSettings ++ releaseSettings: _*)
    .settings(
      releaseProcess <<= thisProjectRef {
        ref =>
          Seq[ReleaseStep](
            checkSnapshotDependencies,
            inquireVersions,
            runTest,
            setReleaseVersion,
            commitReleaseVersion,
            blessReleaseNotesReleaseStep(ref),
            commitReleaseNotesChanges,
            tagRelease,
            publishArtifacts,
            setNextVersion,
            commitNextVersion,
            pushChanges
          )
      })
    .enablePlugins(MdReleaseNotesFormat, RootFolderReleaseNotesStrategy)

  lazy val blessReleaseNotesReleaseStep = (ref: ProjectRef) => ReleaseStep(
    action = releaseTask(blessReleaseNotes.in(ref))
  )

  lazy val commitReleaseNotesChanges = ReleaseStep(commitReleaseNotesChangesFunc)

  private def vcs(st: State): Vcs = {
    Project
      .extract(st)
      .get(versionControlSystem)
      .getOrElse(sys.error("Aborting release. Working directory is not a repository of a recognized VCS."))
  }

  val commitReleaseNotesChangesFunc = { st: State =>
    val blessedFile = Project.extract(st).get(releaseNotesBlessedFile)
    val releaseNotesDir = Project.extract(st).get(releaseNotesSourceDir)
    val base = vcs(st).baseDir

    blessedFile.foreach(addFileToVcs(st, base, _))
    addAllFilesUnderToVcs(st, base, releaseNotesDir)

    val status = (vcs(st).status !!) trim

    if (status.nonEmpty) {
      vcs(st).commit("Prepared release notes.") ! st.log
    }
    st
  }

  private def addFileToVcs(st: State, base: File, file: File): String = {
    val relativePath = IO.relativize(base, file).getOrElse(s"The release notes file '$file' is outside of this VCS repository with base directory '$base'!")
    vcs(st).add(relativePath) !! st.log
  }

  private def addAllFilesUnderToVcs(st: State, base: File, file: File): String = {
    val relativePath = IO.relativize(base, file).getOrElse(s"The release notes file '$file' is outside of this VCS repository with base directory '$base'!")
    vcs(st).cmd(Seq("add", "-A", relativePath): _*) !! st.log
  }

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