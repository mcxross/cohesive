import org.jetbrains.compose.compose

val ktorVersion: String by project
val serializationVersion: String by project

plugins {
    kotlin("multiplatform")
    kotlin("kapt")
    kotlin("plugin.serialization") version "1.6.10"
    id("org.jetbrains.compose")
    id("com.android.library")
}

group = "com.mcxross.cohesive"
version = "1.0-SNAPSHOT"

kotlin {
    android()
    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(compose.materialIconsExtended)
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$serializationVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            kotlin.srcDirs("src/jvmMain/kotlin")
            dependencies {
                api("androidx.appcompat:appcompat:1.4.2")
                api("androidx.core:core-ktx:1.8.0")
                compileOnly("androidx.activity:activity-compose:1.5.1")
                implementation("io.ktor:ktor-client-okhttp:$ktorVersion")
            }
        }
        val androidTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
        val desktopMain by getting {
            kotlin.srcDirs("src/jvmMain/kotlin")
            dependencies {
                api(compose.preview)
                implementation("org.pf4j:pf4j:3.7.0")
                configurations["kapt"].dependencies.add(implementation("org.pf4j:pf4j:3.7.0"))
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
            }
        }
        val desktopTest by getting {

        }
    }
}


android {
    compileSdk = 33
    defaultConfig {
        minSdk = 24
        targetSdk = 33
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    sourceSets {
        named("main") {
            manifest.srcFile("src/androidMain/AndroidManifest.xml")
            res.srcDirs("src/androidMain/res", "src/commonMain/resources")
        }
    }
}

