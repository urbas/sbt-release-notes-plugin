addSbtPlugin("com.typesafe.sbt" % "sbt-site" % "0.8.1")

addSbtPlugin("si.urbas" % "sbt-release-notes-plugin" % IO.read(file("../../version")))
