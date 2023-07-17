plugins {
  id("org.jetbrains.compose")
  id("com.android.application")
  id("org.jetbrains.dokka")
  kotlin("android")
}

group

"xyz.mcxross.cohesive"

version

"0.1.0-alpha"

repositories { mavenCentral() }

dependencies {
  implementation(project(":cmpe:common"))
  implementation("androidx.activity:activity-compose:1.5.1")
}

android {
  compileSdk = 33
  defaultConfig {
    applicationId = "xyz.mcxross.cohesive.android"
    minSdk = 24
    targetSdk = 33
    versionCode = 1
    versionName = "0.1.0-alpha"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  buildTypes { getByName("release") { isMinifyEnabled = false } }
}
