import sbt.ScriptedPlugin._
import sbt.Keys._
import sbt._

object BuildConfiguration extends Build {


  override def settings: Seq[Def.Setting[_]] = {
    super.settings ++ Seq(
      organization := "si.urbas",
      publishArtifact.in(Compile, packageDoc) := false
    )
  }

  lazy val root = project.in(file("."))
    .aggregate(releaseNotesPlugin)

  lazy val releaseNotesPlugin = project.in(file("releaseNotesPlugin"))
    .settings(scriptedSettings: _*)
    .settings(
      name := "sbt-release-notes-plugin",
      sbtPlugin := true,
      scriptedLaunchOpts ++= Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + version.value),
      scriptedBufferLog := false
    )

}