package si.urbas.sbt.content

import java.io.File

import sbt.IO

case class FileContent(file: File) extends TimestampedContent {
  override def content: String = if (file.exists()) IO.read(file) else ""
  override val timestamp: Long = if (file.exists()) file.lastModified() else Long.MinValue
}
