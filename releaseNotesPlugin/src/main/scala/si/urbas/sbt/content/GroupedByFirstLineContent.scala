package si.urbas.sbt.content

import GroupedByFirstLineContent._

import scala.collection.mutable

case class GroupedByFirstLineContent(compoundContent: CompoundContent) extends TimestampedContent {

  override def content: String = {
    compoundContent
      .contents
      .foldLeft(emptyGroups)(addEntryToGroups)
      .map(concatPairOfStrings)
      .mkString("\n\n")
  }

  override val timestamp: Long = compoundContent.timestamp

}

object GroupedByFirstLineContent {

  private def emptyGroups: mutable.Map[String, String] = {
    mutable.LinkedHashMap.empty[String, String].withDefault(_ => "")
  }

  private def addEntryToGroups(groups: mutable.Map[String, String], entryContent: TimestampedContent): mutable.Map[String, String] = {
    val entryString = entryContent.content
    val indexOfNewline = entryString.indexOf('\n')
    val groupTitle = entryString.take(indexOfNewline)
    groups += (groupTitle -> (groups(groupTitle) + entryString.drop(indexOfNewline)))
  }

  private def concatPairOfStrings(s: (String, String)): String = {
    s._1 + s._2
  }

}
