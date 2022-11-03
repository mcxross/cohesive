plugins {
  id("org.jetbrains.compose")
  id("com.google.devtools.ksp") version "1.7.10-1.0.6"
}

dependencies {
  compileOnly(kotlin("stdlib"))
  compileOnly(project(":common"))
  compileOnly(project(":csp"))
  ksp(project(":csp"))
}
