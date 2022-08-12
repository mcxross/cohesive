package com.mcxross.cohesive.common.openapi

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowScope
import org.pf4j.ExtensionPoint

interface IMainScreen: ExtensionPoint {
    @Composable
    fun WindowButtons()

    @Composable
    fun WindowListMenuButton()

    @Composable
    fun SwitchView()

    @Composable
    fun ClusterMenu()

    @Composable
    fun TitleMenuBar(windowScope: WindowScope)

    @Composable
    fun Show(windowScope: WindowScope)

}