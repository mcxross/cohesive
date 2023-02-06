package com.mcxross.cohesive.common.utils

import java.awt.Dimension
import java.awt.Toolkit

fun dimension() : Dimension {
  val screenSize = Toolkit.getDefaultToolkit().screenSize
  return Dimension(screenSize.width, screenSize.height)
}

fun splashScreenSize() : Dimension {
  val screenSize = Toolkit.getDefaultToolkit().screenSize
  return Dimension((screenSize.width * 0.6).toInt(), (screenSize.height * 0.6).toInt())
}
