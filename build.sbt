
val commonSettings: Seq[sbt.Def.Setting[_]] = Seq(
  organization := "com.codacy",
  scalaVersion := "2.11.12",
  scalacOptions ++= Seq(
    "-Xexperimental",
    "-Ypartial-unification"
  )
)

val akkaHttpVersion   = "10.0.15"
val akkaVersion       = "2.4.20"

val catsVersion       = "1.5.0"
val circeVersion      = "0.9.1"

lazy val client = (project in file("client"))
  .settings(commonSettings)
  .settings(
    guardrailTasks in Compile := List(
      Client(file(".") / "swagger" / "petstore.yaml", pkg="com.codacy.petstore.client")
    ),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"           % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"         % akkaVersion,

      "io.circe"          %% "circe-core"          % circeVersion,
      "io.circe"          %% "circe-generic"       % circeVersion,
      "io.circe"          %% "circe-java8"         % circeVersion,
      "io.circe"          %% "circe-parser"        % circeVersion,
      "org.typelevel"     %% "cats-core"           % catsVersion
    )
  )

lazy val server = (project in file("server"))
  .settings(
    guardrailTasks in Compile := List(
      Server(file(".") / "swagger" / "petstore.yaml", pkg="com.codacy.petstore.server")
    ),
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"           % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"         % akkaVersion,

      "io.circe"          %% "circe-core"          % circeVersion,
      "io.circe"          %% "circe-generic"       % circeVersion,
      "io.circe"          %% "circe-java8"         % circeVersion,
      "io.circe"          %% "circe-parser"        % circeVersion,
      "org.typelevel"     %% "cats-core"           % catsVersion
    )
  )

lazy val root = (project in file("."))
  .aggregate(client, server)
