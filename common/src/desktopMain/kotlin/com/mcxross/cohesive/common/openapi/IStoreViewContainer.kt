package com.mcxross.cohesive.common.openapi

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.model.Plugin
import org.pf4j.ExtensionPoint

interface IStoreViewContainer: ExtensionPoint {
    @Composable
    fun StoreView(windowScope: WindowScope, plugins: List<Plugin>)
}