import com.lightbend.lagom.sbt.LagomPlugin.autoImport.lagomKafkaEnabled

organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.5"

lazy val `restsrv` = (project in file("."))
  .aggregate(
    `restsrv-api`, `restsrv-impl`
  ).enablePlugins(Cinnamon)

lazy val `restsrv-api` = (project in file("restsrv-api"))
  .settings(common: _*)
  .settings(
    resolvers += Cinnamon.resolver.commercial,
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `restsrv-impl` = (project in file("restsrv-impl"))
  .enablePlugins(LagomJava, Cinnamon)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomLogback,
      lagomJavadslTestKit,
      lagomJavadslPersistenceJdbc,
      lombok,
      sbr
//      `akka-cluster-tools`

    ) ++ jdbi ++ h2 ++ cinnamonDependencies
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`restsrv-api`)


val akkaVersion = "2.5.11"
val lombok = "org.projectlombok" % "lombok" % "1.16.18"
val `akka-cluster-tools` = "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion
val sbr = "com.lightbend.akka" %% "akka-split-brain-resolver" % "1.1.0"

val jdbiVersion = "3.1.0"
val jdbi = Seq(
  "org.jdbi" % "jdbi3-sqlobject" % jdbiVersion,
  "org.jdbi" % "jdbi3-core" % jdbiVersion
)

val h2 = Seq("com.h2database" % "h2" % "1.4.196")

def common = Seq(
  // this setting required if use Sql Objects and bind query parameters to method parameters by their names
  javacOptions in compile += "-parameters"
)

lazy val cinnamonDependencies = Seq(
  // Use Coda Hale Metrics and Lagom instrumentation
  Cinnamon.library.cinnamonCHMetrics,
  Cinnamon.library.cinnamonLagom,
  Cinnamon.library.cinnamonAkkaHttp,
  Cinnamon.library.cinnamonAkka,
  Cinnamon.library.cinnamonPrometheus,
  Cinnamon.library.cinnamonPrometheusHttpServer
)

//lagomServiceLocatorEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false
lagomCassandraEnabled in ThisBuild := false

// Add the Cinnamon Agent for run and test
//cinnamon in run := true
//cinnamon in test := true

// Set the Cinnamon Agent log level
cinnamonLogLevel := "INFO"
