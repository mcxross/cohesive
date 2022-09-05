package com.mcxross.cohesive.common.openapi.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.model.Plugin
import com.mcxross.cohesive.common.openapi.ui.view.IView

interface IStore : IView {
    @Composable
    fun Emit(windowScope: WindowScope, plugins: List<Plugin>)
}