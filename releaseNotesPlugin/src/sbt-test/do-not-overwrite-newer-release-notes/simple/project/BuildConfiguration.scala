import sbt._
import si.urbas.sbt.releasenotes._
import si.urbas.sbt.releasenotes.test._

object ThrowIfReleaseNotesWritten extends ReleaseNotesStrategy {

  import si.urbas.sbt.releasenotes.ReleaseNotesPlugin.autoImport._

  override def projectSettings: Seq[Def.Setting[_]] = {
    Seq(
      releaseNotesCurrentVersionBody <<= releaseNotesCurrentVersionBody.map(_.map(_ => sys.error(s"The content of release notes was overwritten.")))
    )
  }

}

object BuildConfiguration extends Build {
  val root = project.in(file("."))
    .enablePlugins(RstReleaseNotesFormat, ReleaseNotesSelfTestPlugin, ThrowIfReleaseNotesWritten)
}