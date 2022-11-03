import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val pluginDirDesktop: File by rootProject.extra

plugins {
  kotlin("multiplatform")

  id("org.jetbrains.compose")
  id("org.jetbrains.dokka")
}

group = "com.mcxross.cohesive"

version = "0.1.0"

kotlin {
  jvm {
    compilations.all { kotlinOptions.jvmTarget = "11" }
    withJava()
  }
  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(project(":common"))
        implementation(project(":csp"))
        implementation(compose.desktop.currentOs)
      }
    }
  }
}

compose.desktop {
  application {
    System.setProperty("cps.pluginDir", pluginDirDesktop.absolutePath)
    mainClass = "com.mcxross.cohesive.desktop.MainKt"
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "cohesive"
      packageVersion = "1.0.0"
    }
  }
}
