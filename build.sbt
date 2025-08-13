// build.sbt
ThisBuild / version := "1.0"
ThisBuild / scalaVersion := "2.13.12"
ThisBuild / organization := "org.example"

val spinalVersion = "1.10.1"
//fork in test := true // Важно для Windows!
//javaOptions in test += "-Djava.library.path=/c/msys64/mingw64/bin" // Путь к бинарникам

lazy val root = (project in file("."))
  .settings(
    name := "Aleste",
    libraryDependencies ++= Seq(
      "com.github.spinalhdl" %% "spinalhdl-core" % spinalVersion,
      "com.github.spinalhdl" %% "spinalhdl-lib" % spinalVersion,
      "org.scalatest" %% "scalatest" % "3.2.15" % Test
   ),
    addCompilerPlugin("com.github.spinalhdl" %% "spinalhdl-idsl-plugin" % spinalVersion),
    scalacOptions += "-Ymacro-annotations",
    fork := true
  )

