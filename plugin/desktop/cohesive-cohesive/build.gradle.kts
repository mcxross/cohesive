plugins {
  id("org.jetbrains.compose")
  id("com.google.devtools.ksp") version "1.8.0-1.0.9"
}

dependencies {
  compileOnly(kotlin("stdlib"))
  compileOnly(project(":common"))
  compileOnly(project(":csp"))
  ksp(project(":csp"))
}
