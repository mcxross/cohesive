val pluginDirDesktop: File by rootProject.extra

plugins { kotlin("jvm") }

subprojects {
  val pluginId: String by project
  val pluginClass: String by project
  val pluginProvider: String by project

  val project = this

  apply(plugin = "org.jetbrains.kotlin.jvm")

  tasks.register<Jar>("plugin") {
    archiveBaseName.set(pluginId)

    into("classes") { with(tasks.named<Jar>("jar").get()) }

    dependsOn(configurations.runtimeClasspath)
    into("lib") {
      from({ configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") } })
    }
    archiveExtension.set("zip")
  }

  tasks.register<Copy>("assemblePlugin") {
    from(project.tasks.named("plugin"))
    into(pluginDirDesktop)
  }

  tasks.named<Jar>("jar") {
    manifest {
      attributes["Plugin-Class"] = pluginClass
      attributes["Plugin-Id"] = pluginId
      attributes["Plugin-Version"] = archiveVersion
      attributes["Plugin-Provider"] = pluginProvider
    }
  }

  tasks.named("build") { dependsOn(tasks.named("plugin")) }
}

tasks.register<Copy>("assemblePlugins") {
  dependsOn(subprojects.map { it.tasks.named("assemblePlugin") })
}

tasks { "build" { dependsOn(named("assemblePlugins")) } }
