package com.mcxross.cohesive.common.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.WindowState
import com.mcxross.cohesive.common.view.View

object WindowStateHolder {
    var state: WindowState = WindowState()
    var view: Any? by mutableStateOf(View.EXPLORER)
    var isWindowOpen: Boolean by mutableStateOf(true)
}