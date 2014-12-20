resolvers ++= Seq(
  "Sonatype Public Releases" at "https://oss.sonatype.org/content/groups/public/",
  "Sonatype Public Snaphots" at "https://oss.sonatype.org/content/repositories/snapshots/"
)

libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.2.1")

addSbtPlugin("com.github.gseitz" % "sbt-release" % "0.8.5")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")

addSbtPlugin("si.urbas" % "sbt-release-notes-plugin" % "0.0.2")