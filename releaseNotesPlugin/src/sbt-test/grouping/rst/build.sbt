import si.urbas.sbt.releasenotes.formats

enablePlugins(
  formats.RstReleaseNotesFormat,
  GroupReleaseNotesByFirstLine,
  ReleaseNotesSelfTestPlugin
)