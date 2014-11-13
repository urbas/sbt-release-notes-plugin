package si.urbas.sbt

import java.io.File

import sbt.{Def, IO}
import si.urbas.sbt.util._

package object content {

  def toContent(files: Seq[File], separator: String = ""): CompoundContent = {
    CompoundContent(files.sortBy(_.getName).map(FileContent.apply), separator)
  }

  def overwriteIfOlder(outputFile: File, content: TimestampedContent): Unit = {
    ifSourceNewer(content, outputFile)((content, file) => IO.write(file, content.content))
  }

  def overwrite(outputFile: File, content: TimestampedContent): Unit = {
    IO.write(outputFile, content.content)
  }

  def toContentDef(string: Def.Initialize[String]): Def.Initialize[TimestampedContent] = {
    string(StringContent.apply)
  }

  def toContentDef(string: String): Def.Initialize[TimestampedContent] = {
    Def.value(StringContent(string))
  }

}
