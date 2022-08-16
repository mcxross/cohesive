package com.mcxross.cohesive.common.openapi

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.model.Chain
import org.pf4j.ExtensionPoint

interface IStoreView: ExtensionPoint {
    @Composable
    fun View(windowScope: WindowScope, chains: List<Chain>)
}