pluginManagement {
  repositories {
    google()
    gradlePluginPortal()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }

  plugins {
    kotlin("jvm").version(extra["kotlin.version"] as String)
    kotlin("multiplatform").version(extra["kotlin.version"] as String)
    kotlin("android").version(extra["kotlin.version"] as String)
    kotlin("plugin.serialization").version(extra["kotlin.version"] as String)
    id("com.android.base").version(extra["agp.version"] as String)
    id("com.android.application").version(extra["agp.version"] as String)
    id("com.android.library").version(extra["agp.version"] as String)
    id("org.jetbrains.compose").version(extra["compose.version"] as String)
    id("org.jetbrains.kotlin.plugin.allopen").version(extra["kotlin.version"] as String)
    id("com.google.devtools.ksp").version(extra["ksp.version"] as String)
    id("org.jetbrains.kotlinx.benchmark").version(extra["benchmark.version"] as String)
  }
}

rootProject.name = "cohesive"

include(
  ":cmpe:android",
  ":cmpe:desktop",
  ":cmpe:common",
  ":cmpe:web",
  ":cmpe:csp",
  ":cmpe:plugin",
  ":cmpe:plugin:desktop",
  ":cmpe:plugin:desktop:cohesive-cohesive",
  ":seabed",
)
