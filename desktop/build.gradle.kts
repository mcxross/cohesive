import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("kapt")
    id("org.jetbrains.compose")
}

group = "com.mcxross.cohesive"
version = "1.0-SNAPSHOT"

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "11"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation(compose.desktop.currentOs)
                implementation("org.pf4j:pf4j:3.7.0")
                configurations["kapt"].dependencies.add(implementation("org.pf4j:pf4j:3.7.0"))
            }
        }

    }
}

compose.desktop {
    application {
        mainClass = "com.mcxross.cohesive.desktop.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "cohesive"
            packageVersion = "1.0.0"
        }
    }
}
