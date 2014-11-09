package si.urbas.sbt.content

case class MappedContent(contentToMap: TimestampedContent, map: String => String) extends TimestampedContent {
  override def content: String = map(contentToMap.content)
  override val timestamp: Long = contentToMap.timestamp
}
