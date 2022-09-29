package com.mcxross.cohesive.common.frontend.utils

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.window.WindowState
import com.mcxross.cohesive.common.frontend.ui.view.View
import com.mcxross.cohesive.mellow.HomeFolder

object WindowStateHolder {
    var state: WindowState = WindowState()
    var view: Any? by mutableStateOf(View.EXPLORER)
    var isMainWindowOpen: Boolean by mutableStateOf(true)
    var isImportAccountOpen: Boolean by mutableStateOf(false)
    var isCreateAccountOpen: Boolean by mutableStateOf(false)
    var isOpenDialogOpen: Boolean by mutableStateOf(false)
    var isStoreWindowOpen: Boolean by mutableStateOf(true)
    var isPreAvail: Boolean by mutableStateOf(false)
    var isDelayClose: Boolean by mutableStateOf(true)
    var currentProjectFile: Any? by mutableStateOf(HomeFolder.absolutePath)
}