import org.jetbrains.compose.compose

val ktorVersion: String by project
val serializationVersion: String by project
val okioVersion: String by project
val napierVersion: String by project

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.kotlinx.benchmark") version "0.4.4"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.10"
    id("com.google.devtools.ksp") version "1.6.10-1.0.4"
    kotlin("plugin.serialization") version "1.6.10"
    id("org.jetbrains.compose")
    id("com.android.library")
    id("org.jetbrains.dokka")
}

group = "com.mcxross.cohesive"
version = "0.1.0"

allOpen {
    annotation("org.openjdk.jmh.annotations.State")
}

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
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
                implementation("com.squareup.okio:okio:$okioVersion")
                implementation("com.akuleshov7:ktoml-core:0.2.11")
                implementation("io.github.aakira:napier:$napierVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
                implementation("org.jetbrains.kotlinx:kotlinx-benchmark-runtime:0.4.5")
                implementation("io.github.xxfast:kstore:0.1")
                implementation("org.jetbrains:markdown:0.3.1")
                implementation("org.kodein.di:kodein-di:7.15.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("io.ktor:ktor-client-mock:$ktorVersion")
                implementation("com.squareup.okio:okio-fakefilesystem:$okioVersion")
            }
        }
        val commonBenchmark by creating {
            dependsOn(commonMain)
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
        val androidBenchmark by creating {
            dependsOn(androidMain)
        }
        val desktopMain by getting {
            kotlin.srcDirs("src/jvmMain/kotlin")
            dependencies {
                api(compose.preview)
                implementation(project(":csp"))
                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-cli:0.3.5")
                implementation(kotlin("reflect"))
                implementation("com.squareup:kotlinpoet-ksp:1.12.0")
                implementation("org.ow2.asm:asm:9.4")
            }
        }
        val desktopTest by getting {

        }
        val desktopBenchmark by creating {
            dependsOn(desktopMain)
        }
    }
}

dependencies {
    add("kspDesktop", project(":csp"))
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

benchmark {
    targets {
        register("commonBenchmark")
    }
}
