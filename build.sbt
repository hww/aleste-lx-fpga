// build.sbt
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.13.12"
ThisBuild / organization := "org.example"

val spinalVersion = "1.9.4"

lazy val root = (project in file("."))
  .settings(
    name := "Aleste",
    libraryDependencies ++= Seq(
      "org.scala-lang.modules" %% "scala-java8-compat" % "1.0.2",
      "com.github.spinalhdl" %% "spinalhdl-core" % spinalVersion,
      "com.github.spinalhdl" %% "spinalhdl-lib" % spinalVersion,
      "com.github.spinalhdl" %% "spinalhdl-sim" % spinalVersion % Test,
      "org.scalatest" %% "scalatest" % "3.2.15" % Test
   ),
    addCompilerPlugin("com.github.spinalhdl" %% "spinalhdl-idsl-plugin" % spinalVersion),
    scalacOptions += "-Ymacro-annotations",
    // Увеличиваем память и параллелизм
    fork := true,
    javaOptions ++= Seq("-Xmx4G", "-Xss8M", "-XX:MaxMetaspaceSize=1G"),
    scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-language:reflectiveCalls"),
  )
