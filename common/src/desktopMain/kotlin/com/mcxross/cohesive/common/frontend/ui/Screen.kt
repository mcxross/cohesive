package com.mcxross.cohesive.common.frontend.ui

import java.awt.Toolkit

actual fun getScreenSize(): Pair<Int, Int> {
    val toolkit = Toolkit.getDefaultToolkit()
    val screenSize = toolkit.screenSize
    return Pair(screenSize.width, 100)
}

fun demo(string: String) {

}