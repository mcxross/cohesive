package xyz.mcxross.cohesive.utils

import java.awt.Toolkit

actual fun getScreenSize(): Pair<Int, Int> {
  val toolkit = Toolkit.getDefaultToolkit()
  val screenSize = toolkit.screenSize
  return Pair(screenSize.width, 100)
}
