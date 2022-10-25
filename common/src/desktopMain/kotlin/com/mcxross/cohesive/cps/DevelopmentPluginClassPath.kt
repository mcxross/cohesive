package com.mcxross.cohesive.cps

/**
 * It's a compound [PluginClasspath] ([.MAVEN] + [.GRADLE] + [.KOTLIN])
 * used in development mode ([RuntimeMode.DEVELOPMENT]).
 */
class DevelopmentPluginClasspath : PluginClasspath() {
    init {
        addClassesDirectories(MAVEN.classesDirectories)
        addClassesDirectories(GRADLE.classesDirectories)
        addClassesDirectories(KOTLIN.classesDirectories)
        addClassesDirectories(IDEA.classesDirectories)
        addJarsDirectories(MAVEN.jarsDirectories)
        addJarsDirectories(GRADLE.jarsDirectories)
        addJarsDirectories(KOTLIN.jarsDirectories)
        addJarsDirectories(IDEA.jarsDirectories)
    }

    companion object {
        /**
         * The development plugin classpath for [Maven](https://maven.apache.org).
         * The classes directory is `target/classes` and the lib directory is `target/lib`.
         */
        val MAVEN: PluginClasspath =
            PluginClasspath().addClassesDirectories("target/classes").addJarsDirectories("target/lib")

        /**
         * The development plugin classpath for [Gradle](https://gradle.org).
         * The classes directories are `build/classes/java/main, build/resources/main`.
         */
        val GRADLE: PluginClasspath =
            PluginClasspath().addClassesDirectories("build/classes/java/main", "build/resources/main")

        /**
         * The development plugin classpath for [Kotlin](https://kotlinlang.org).
         * The classes directories are `build/classes/kotlin/main", build/resources/main, build/tmp/kapt3/classes/main`.
         */
        val KOTLIN: PluginClasspath = PluginClasspath()
            .addClassesDirectories("build/classes/kotlin/main", "build/resources/main", "build/tmp/kapt3/classes/main")

        /**
         * The development plugin classpath for [IDEA](https://www.jetbrains.com/help/idea/specifying-compilation-settings.html).
         * The classes directories are `out/production/classes", out/production/resource`.
         */
        val IDEA: PluginClasspath =
            PluginClasspath().addClassesDirectories("out/production/classes", "out/production/resource")
    }
}