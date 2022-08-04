package com.mcxross.cohesive.common.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.WindowState
import com.mcxross.cohesive.common.common.View

object WindowStateHolder {
    var state: WindowState = WindowState()
    var view: Any? by mutableStateOf(View.EXPLORER)
    var isWindowOpen: Boolean by mutableStateOf(true)
    var isImportAccountOpen: Boolean by mutableStateOf(false)
    var isCreateAccountOpen: Boolean by mutableStateOf(false)
    var isOpenDialogOpen: Boolean by mutableStateOf(false)
}