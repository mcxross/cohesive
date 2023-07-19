val napierVersion: String by project
val okioVersion: String by project

plugins {
  `java-library`
  `maven-publish`
  kotlin("jvm")
  kotlin("plugin.serialization") version "1.8.0"
  id("org.jetbrains.dokka")
}

group = "xyz.mcxross.cohesive.csp"

version = "0.1.0-alpha"

repositories { mavenCentral() }

dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.google.devtools.ksp:symbol-processing-api:1.8.0-1.0.9")
  implementation(kotlin("reflect"))
  implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

sourceSets.main { java.srcDirs("src/main/kotlin") }

publishing {
  publications {
    create<MavenPublication>("maven") {
      groupId = "com.mcxross.cohesive"
      artifactId = "cohesive-csp"
      from(components["java"])
    }
  }

  repositories {
    maven {
      name = "GitHubPackages"
      url = uri(layout.buildDirectory.dir("repo"))
    }
  }
}
