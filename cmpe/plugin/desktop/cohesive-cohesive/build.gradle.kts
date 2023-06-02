plugins {
  id("org.jetbrains.compose")
  id("com.google.devtools.ksp")
}

dependencies {
  compileOnly(kotlin("stdlib"))
  compileOnly(project(":cmpe:common"))
  compileOnly(project("::cmpe:csp"))
  ksp(project(":cmpe:csp"))
}
