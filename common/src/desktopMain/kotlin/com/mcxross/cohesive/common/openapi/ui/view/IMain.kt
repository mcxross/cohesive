package com.mcxross.cohesive.common.openapi.ui.view

import androidx.compose.runtime.Composable
import androidx.compose.ui.window.WindowScope
import org.pf4j.ExtensionPoint

interface IMain : IView {
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