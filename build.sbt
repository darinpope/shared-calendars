name := """shared-calendars"""

version := "1.0-SNAPSHOT"

javaOptions in Test ++= Seq(
  "-Dconfig.file=conf/test.conf"
)

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.6"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  filters,
  "com.amazonaws" % "aws-java-sdk-cloudsearch" % "1.10.22",
  "com.amazonaws" % "aws-java-sdk-s3" % "1.10.22",
  "net.sf.biweekly" % "biweekly" % "0.4.3",
  "com.netflix.archaius" % "archaius-aws" % "0.7.1"
)

// Play provides two styles of routers, one expects its actions to be injected, the
// other, legacy style, accesses its actions statically.
routesGenerator := InjectedRoutesGenerator
