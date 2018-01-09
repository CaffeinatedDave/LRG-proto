name := "evaded-memory"

version := "0.1"

scalaVersion := "2.11.8"

mainClass in (Compile, run) := Some("com.caffeinateddave.AkkaHttpApp")

val akkaVersion = "2.4.16"
val akkaHttpVersion = "10.0.3"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "org.json4s" %% "json4s-native" % "3.5.3",
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
)

herokuFatJar in Compile := Some((assemblyOutputPath in assembly).value)

herokuAppName in Compile := "evaded-memory"

