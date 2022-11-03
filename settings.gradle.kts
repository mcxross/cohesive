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
    id("com.android.base").version(extra["agp.version"] as String)
    id("com.android.application").version(extra["agp.version"] as String)
    id("com.android.library").version(extra["agp.version"] as String)
    id("org.jetbrains.compose").version(extra["compose.version"] as String)
  }
}

rootProject.name = "cohesive"

include(
  ":android",
  ":desktop",
  ":common",
  ":web",
  ":csp",
  ":plugin",
  ":plugin:desktop",
  ":plugin:desktop:cohesive-cohesive"
)
