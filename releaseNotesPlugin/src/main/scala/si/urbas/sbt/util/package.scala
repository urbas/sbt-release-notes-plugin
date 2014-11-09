package si.urbas.sbt

import sbt._
import si.urbas.sbt.content.TimestampedContent

package object util {

  def ifSourceNewer[T](source: File, targetFile: File)(callback: (File, File) => Unit): Unit = {
    if (!targetFile.exists() || targetFile.lastModified() < source.lastModified()) {
      callback(source, targetFile)
    }
  }


  def ifSourceNewer[T](source: TimestampedContent, targetFile: File)(callback: (TimestampedContent, File) => Unit): Unit = {
    if (!targetFile.exists() || targetFile.lastModified() < source.timestamp) {
      callback(source, targetFile)
    }
  }

}
