import org.jetbrains.compose.desktop.application.dsl.TargetFormat

val pluginDirDesktop: File by rootProject.extra

plugins {
  kotlin("multiplatform")

  id("org.jetbrains.compose")
}

group = "com.mcxross.cohesive"

version = "0.1.0-alpha"

kotlin {
  jvm {
    compilations.all { kotlinOptions.jvmTarget = "11" }
    withJava()
  }
  sourceSets {
    val jvmMain by getting {
      dependencies {
        implementation(project(":cmpe:common"))
        implementation(project(":cmpe:csp"))
        implementation(compose.desktop.currentOs)
      }
    }
  }
}

compose.desktop {
  application {
    mainClass = "xyz.mcxross.cohesive.desktop.MainKt"
    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "cohesive"
      packageVersion = "1.0.0"
      version = "0.1.0-alpha"
      description = "Blockchain Agnostic Development Platform"
      vendor = "Mcxross"
      licenseFile.set(rootProject.file("LICENSE"))

      macOS {}
      windows {
        console = true
        dirChooser = true
        perUserInstall = true
        menuGroup = "Mcxross"
        upgradeUuid = "70bdd2db-856c-4770-b936-1dca129752c2"
      }
      linux {}

    }
  }
}
