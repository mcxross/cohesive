import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val pluginDirDesktop by extra { file("$projectDir/cmpe/desktop/build/plugin/sec") }

group = "com.mcxross.cohesive"
version = "0.1.0"

allprojects {
  repositories {
    google()
    mavenCentral()
    mavenLocal()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }
}

plugins {
  kotlin("jvm") apply false
  kotlin("multiplatform") apply false
  kotlin("android") apply false
  id("com.android.application") apply false
  id("com.android.library") apply false
  id("org.jetbrains.compose") apply false
  id("org.jetbrains.dokka") version "1.7.10" apply true
}

subprojects {
  repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
  }

  plugins.withId("org.jetbrains.kotlin.multiplatform") {
    tasks.withType<KotlinCompile> {
      kotlinOptions.freeCompilerArgs += "-opt-in=kotlin.RequiresOptIn"
    }
  }
}
