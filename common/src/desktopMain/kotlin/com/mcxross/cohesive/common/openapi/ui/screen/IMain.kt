package com.mcxross.cohesive.common.openapi.ui.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.openapi.ui.view.IView
import com.mcxross.cohesive.common.ui.view.editor.Editor
import com.mcxross.cohesive.common.ui.view.explorer.Explorer
import com.mcxross.cohesive.common.ui.view.wallet.Wallet
import com.mcxross.cohesive.common.utils.WindowStateHolder

interface IMain : IView {

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

    @Composable
    fun ExplorerView()

    @Composable
    fun WalletView()

    @Composable
    fun EditorView()

    @Composable
    fun CreateAccountDialog(
        onClose: () -> Unit,
        text: String,
        negativeText: String,
        neutralText: String,
        positiveText: String,
        onNegative: () -> Unit,
        onNeutral: () -> Unit,
        onPositive: () -> Unit,
        negativeEnable: Boolean,
        neutralEnable: Boolean,
        positiveEnable: Boolean,
        width: Dp,
        height: Dp,
        content: @Composable () -> Unit,
    )

    @Composable
    fun ImportAccountDialog(
        onClose: () -> Unit,
        text: String,
        negativeText: String,
        neutralText: String,
        positiveText: String,
        onNegative: () -> Unit,
        onNeutral: () -> Unit,
        onPositive: () -> Unit,
        negativeEnable: Boolean,
        neutralEnable: Boolean,
        positiveEnable: Boolean,
        width: Dp,
        height: Dp,
        content: @Composable () -> Unit
    )
}