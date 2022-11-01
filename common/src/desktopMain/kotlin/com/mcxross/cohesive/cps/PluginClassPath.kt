package com.mcxross.cohesive.cps

import java.util.*


/**
 * The classpath of the corePlugin.
 * It contains `classes` directories (directories that contain classes files)
 * and `jars` directories (directories that contain jars files).
 */
open class PluginClasspath {
  val classesDirectories: MutableSet<String> = HashSet()
  val jarsDirectories: MutableSet<String> = HashSet()

  fun addClassesDirectories(vararg classesDirectories: String): PluginClasspath {
    return addClassesDirectories(listOf(*classesDirectories))
  }

  fun addClassesDirectories(classesDirectories: Collection<String>): PluginClasspath {
    this.classesDirectories.addAll(classesDirectories)
    return this
  }

  fun addJarsDirectories(vararg jarsDirectories: String): PluginClasspath {
    return addJarsDirectories(mutableListOf(*jarsDirectories))
  }

  fun addJarsDirectories(jarsDirectories: Collection<String>?): PluginClasspath {
    this.jarsDirectories.addAll(jarsDirectories!!)
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is PluginClasspath) return false
    return (classesDirectories == other.classesDirectories) && jarsDirectories == other.jarsDirectories
  }

  override fun hashCode(): Int {
    return Objects.hash(classesDirectories, jarsDirectories)
  }
}
