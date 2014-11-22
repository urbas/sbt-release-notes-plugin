package si.urbas.sbt.content

object NoContent extends TimestampedContent {
  override val timestamp: Long = Long.MinValue

  override def content: String = ""
}
