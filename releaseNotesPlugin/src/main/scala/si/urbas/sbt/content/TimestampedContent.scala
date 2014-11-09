package si.urbas.sbt.content

trait TimestampedContent {
  def content: String

  val timestamp: Long

  def map(f: String => String): TimestampedContent = MappedContent(this, f)

  def +(otherContent: TimestampedContent): TimestampedContent = CompoundContent(Seq(this, otherContent))
}