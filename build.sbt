import com.lightbend.lagom.sbt.LagomPlugin.autoImport.lagomKafkaEnabled

organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

lazy val `hello` = (project in file("."))
  .aggregate(
    `restsrv-api`, `restsrv-impl`
  )

lazy val `restsrv-api` = (project in file("restsrv-api"))
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomJavadslApi,
      lombok
    )
  )

lazy val `restsrv-impl` = (project in file("restsrv-impl"))
  .enablePlugins(LagomJava)
  .settings(common: _*)
  .settings(
    libraryDependencies ++= Seq(
      lagomLogback,
      lagomJavadslTestKit,
      lombok
//      lagomJavadslCluster,
//      `akka-cluster-tools`
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`restsrv-api`)


val akkaVersion = "2.5.11"
val lombok = "org.projectlombok" % "lombok" % "1.16.18"
val `akka-cluster-tools` = "com.typesafe.akka" %% "akka-cluster-tools" % akkaVersion

def common = Seq(
  javacOptions in compile += "-parameters"
)

//lagomServiceLocatorEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false
lagomCassandraEnabled in ThisBuild := false
