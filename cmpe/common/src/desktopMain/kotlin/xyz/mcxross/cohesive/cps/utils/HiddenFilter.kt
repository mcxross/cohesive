package xyz.mcxross.cohesive.cps.utils

import java.io.File
import java.io.FileFilter

/**
 * Filter that only accepts hidden files.
 */
class HiddenFilter : FileFilter {
  override fun accept(file: File): Boolean {
    return file.isHidden
  }
}
