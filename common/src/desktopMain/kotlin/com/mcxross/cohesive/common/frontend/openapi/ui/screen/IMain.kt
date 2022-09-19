package com.mcxross.cohesive.common.frontend.openapi.ui.screen

import androidx.compose.runtime.Composable
import com.mcxross.cohesive.common.frontend.openapi.ui.view.IView

interface IMain : IView {

    @Composable
    fun WindowListMenuButton()

    @Composable
    fun SwitchView()

    @Composable
    fun ClusterMenu()

    @Composable
    fun TitleMenuBar()

    @Composable
    fun ExplorerView()

    @Composable
    fun WalletView()

    @Composable
    fun EditorView()
}