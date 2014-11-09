package si.urbas.sbt

import java.io.File

import sbt.{Def, IO}

package object content {

  def toContent(files: Seq[File], separator: String = ""): TimestampedContent = {
    CompoundContent(files.sortBy(_.getName).map(FileContent.apply), separator)
  }

  def overwriteIfOlder(outputFile: File, content: TimestampedContent) {
    if (outputFile.lastModified() < content.timestamp) {
      IO.write(outputFile, content.content)
    }
  }

  def toContentDef(string: Def.Initialize[String]): Def.Initialize[TimestampedContent] = {
    string(StringContent.apply)
  }

  def toContentDef(string: String): Def.Initialize[TimestampedContent] = {
    Def.value(StringContent(string))
  }

}
