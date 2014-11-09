package si.urbas.sbt.content

case class StringContent(content: String) extends TimestampedContent {
  override val timestamp: Long = Long.MinValue
}
