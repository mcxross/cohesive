@file:Suppress("NewApi")

package xyz.mcxross.cohesive.mellow

import xyz.mcxross.cohesive.common.common.platform.toProjectFile

actual val HomeFolder: File
  get() = java.io.File(System.getProperty("user.home")).toProjectFile()

fun getFileFromPath(path: String): File = java.io.File(path).toProjectFile()
