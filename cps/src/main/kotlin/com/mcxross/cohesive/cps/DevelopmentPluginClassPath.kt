package com.mcxross.cohesive.cps

/**
 * It's a compound [PluginClasspath] ([.MAVEN] + [.GRADLE] + [.KOTLIN])
 * used in development mode ([RuntimeMode.DEVELOPMENT]).
 */
class DevelopmentPluginClasspath : PluginClasspath() {
    init {
        addClassesDirectories(MAVEN.getClassesDirectories())
        addClassesDirectories(GRADLE.getClassesDirectories())
        addClassesDirectories(KOTLIN.getClassesDirectories())
        addClassesDirectories(IDEA.getClassesDirectories())
        addJarsDirectories(MAVEN.getJarsDirectories())
        addJarsDirectories(GRADLE.getJarsDirectories())
        addJarsDirectories(KOTLIN.getJarsDirectories())
        addJarsDirectories(IDEA.getJarsDirectories())
    }

    companion object {
        /**
         * The development holder classpath for [Maven](https://maven.apache.org).
         * The classes directory is `target/classes` and the lib directory is `target/lib`.
         */
        val MAVEN: PluginClasspath =
            PluginClasspath().addClassesDirectories("target/classes").addJarsDirectories("target/lib")

        /**
         * The development holder classpath for [Gradle](https://gradle.org).
         * The classes directories are `build/classes/java/main, build/resources/main`.
         */
        val GRADLE: PluginClasspath =
            PluginClasspath().addClassesDirectories("build/classes/java/main", "build/resources/main")

        /**
         * The development holder classpath for [Kotlin](https://kotlinlang.org).
         * The classes directories are `build/classes/kotlin/main", build/resources/main, build/tmp/kapt3/classes/main`.
         */
        val KOTLIN: PluginClasspath = PluginClasspath()
            .addClassesDirectories("build/classes/kotlin/main", "build/resources/main", "build/tmp/kapt3/classes/main")

        /**
         * The development holder classpath for [IDEA](https://www.jetbrains.com/help/idea/specifying-compilation-settings.html).
         * The classes directories are `out/production/classes", out/production/resource`.
         */
        val IDEA: PluginClasspath =
            PluginClasspath().addClassesDirectories("out/production/classes", "out/production/resource")
    }
}