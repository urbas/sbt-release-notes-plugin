package si.urbas.sbt.releasenotes

import sbt.{Plugins, AutoPlugin}

class ReleaseNotesStrategy extends AutoPlugin {

  override def requires: Plugins = {
    ReleaseNotesPlugin
  }

}
