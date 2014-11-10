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

  override def settings: Seq[Def.Setting[_]] = {
    super.settings ++ Seq(
      organization := "si.urbas"
    ) ++ PublishConfiguration.globalSettings
  }

  lazy val root = project.in(file("."))
    .aggregate(releaseNotesPlugin)
    .settings(PublishConfiguration.rootSettings: _*)
    .settings(ReleaseConfiguration.rootSettings: _*)
    .enablePlugins(MdReleaseNotesFormat, RootFolderReleaseNotesStrategy)

  lazy val releaseNotesPlugin = project.in(file("releaseNotesPlugin"))
    .settings(scriptedSettings ++ sonatypeSettings ++ releaseSettings: _*)
    .settings(
      name := "sbt-release-notes-plugin",
      sbtPlugin := true,
      scriptedLaunchOpts ++= Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + version.value),
      scriptedBufferLog := false
    )

}

object PublishConfiguration {

  private val projectName = "sbt-release-notes-plugin"
  private val projectUrl = s"https://github.com/urbas/$projectName"
  private val projectScmUrl = s"git@github.com:urbas/$projectName.git"
  private val ownerName = "urbas"
  private val ownerUrl = "https://github.com/urbas"
  private val credentialsFile = Path.userHome / ".ivy2" / ".credentials"

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

  def globalSettings: Seq[Setting[_]] = {
    Seq(
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

  def rootSettings: Seq[Setting[_]] = {
    Seq(
      publish := {},
      publishLocal := {}
    ) ++
      sonatypeSettings
  }

}

object ReleaseConfiguration {

  lazy val rootSettings: Seq[Setting[_]] = {
    releaseSettings ++ Seq(
      releaseProcess <<= thisProjectRef {
        thisProject =>
          Seq[ReleaseStep](
            checkSnapshotDependencies,
            inquireVersions,
            runTest,
            setReleaseVersion,
            commitReleaseVersion,
            blessReleaseNotesReleaseStep(thisProject),
            commitReleaseNotesChanges,
            tagRelease,
            publishArtifacts,
            setNextVersion,
            commitNextVersion,
            pushChanges
          )
      })
  }

  private def blessReleaseNotesReleaseStep(ref: ProjectRef): ReleaseStep = {
    ReleaseStep(releaseTask(blessReleaseNotes.in(ref)))
  }

  private lazy val commitReleaseNotesChanges = ReleaseStep(commitReleaseNotesChangesFunc)

  private def vcs(st: State): Vcs = {
    Project
      .extract(st)
      .get(versionControlSystem)
      .getOrElse(sys.error("Aborting release. Working directory is not a repository of a recognized VCS."))
  }

  private def commitReleaseNotesChangesFunc(st: State): State = {
    val blessedFile = Project.extract(st).get(releaseNotesBlessedFile)
    val releaseNotesDir = Project.extract(st).get(releaseNotesSourceDir)
    val base = vcs(st).baseDir

    blessedFile.foreach(addFileToVcs(st, base, _))
    addDirToVcs(st, base, releaseNotesDir)

    val gitStatus = (vcs(st).status !!) trim

    if (gitStatus.nonEmpty) {
      vcs(st).commit("Prepared release notes.") ! st.log
    }
    st
  }

  private def addFileToVcs(st: State, base: File, file: File): String = {
    val relativePath = IO.relativize(base, file).getOrElse(s"The release notes file '$file' is outside of this VCS repository with base directory '$base'!")
    vcs(st).add(relativePath) !! st.log
  }

  private def addDirToVcs(st: State, base: File, dir: File): String = {
    val relativePath = IO.relativize(base, dir).getOrElse(s"The release notes file '$dir' is outside of this VCS repository with base directory '$base'!")
    vcs(st).cmd(Seq("add", "-A", relativePath): _*) !! st.log
  }
}