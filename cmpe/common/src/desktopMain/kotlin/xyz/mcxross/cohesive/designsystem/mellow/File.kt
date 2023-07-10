@file:Suppress("NewApi")

package xyz.mcxross.cohesive.designsystem.mellow

import xyz.mcxross.cohesive.extension.toProjectFile

actual val HomeFolder: File
  get() = java.io.File(System.getProperty("user.home")).toProjectFile()

fun getFileFromPath(path: String): File = java.io.File(path).toProjectFile()
