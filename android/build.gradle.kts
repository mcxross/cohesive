plugins {
  id("org.jetbrains.compose")
  id("com.android.application")
  id("org.jetbrains.dokka")
  kotlin("android")
}

group

"com.mcxross.cohesive"

version

"1.0.0"

repositories { mavenCentral() }

dependencies {
  implementation(project(":common"))
  implementation("androidx.activity:activity-compose:1.5.1")
}

android {
  compileSdk = 33
  defaultConfig {
    applicationId = "com.mcxross.cohesive.android"
    minSdk = 24
    targetSdk = 33
    versionCode = 1
    versionName = "1.0.0"
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  buildTypes { getByName("release") { isMinifyEnabled = false } }
}
