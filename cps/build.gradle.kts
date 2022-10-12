val napierVersion: String by project

plugins {
    kotlin("jvm")
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
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}
