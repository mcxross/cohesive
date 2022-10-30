val napierVersion: String by project
val okioVersion: String by project

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization") version "1.7.10"
  id("org.jetbrains.dokka")
}

group = "com.mcxross.cohesive.csp"

version = "0.1.0"

repositories { mavenCentral() }

dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.google.devtools.ksp:symbol-processing-api:1.7.10-1.0.6")
  implementation(kotlin("reflect"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

sourceSets.main { java.srcDirs("src/main/kotlin") }
