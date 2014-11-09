package si.urbas.sbt.content

case class CompoundContent(contents: Iterable[TimestampedContent], separator: String = "") extends TimestampedContent {
  override def content: String = contents.map(_.content).mkString(separator)
  override val timestamp: Long = (contents.map(_.timestamp) ++ Seq(Long.MinValue)).max
}
