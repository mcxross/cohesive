val napierVersion: String by project
val okioVersion: String by project

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.6.10"
}

group = "com.mcxross.cohesive.cps"
version = "0.1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.ow2.asm:asm:9.4")
    implementation("io.github.aakira:napier:$napierVersion")
    implementation("com.google.devtools.ksp:symbol-processing-api:1.6.10-1.0.4")
    implementation("com.squareup.okio:okio:$okioVersion")
    implementation("com.akuleshov7:ktoml-core:0.2.11")
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}
