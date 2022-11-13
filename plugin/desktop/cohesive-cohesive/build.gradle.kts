plugins {
  id("org.jetbrains.compose")
  id("com.google.devtools.ksp") version "1.7.20-1.0.6"
}

dependencies {
  compileOnly(kotlin("stdlib"))
  compileOnly(project(":common"))
  compileOnly(project(":csp"))
  ksp(project(":csp"))
}
