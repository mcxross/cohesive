package com.mcxross.cohesive.common.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.ds.tree.tree
import com.mcxross.cohesive.common.openapi.ui.screen.IMain
import com.mcxross.cohesive.common.ui.view.View
import com.mcxross.cohesive.common.ui.view.editor.Editor
import com.mcxross.cohesive.common.ui.view.explorer.Explorer
import com.mcxross.cohesive.common.ui.view.wallet.Wallet
import com.mcxross.cohesive.common.utils.WindowStateHolder
import com.mcxross.cohesive.mellow.*
import org.pf4j.Extension

@Extension
open class Main : IMain {

    @Composable
    override fun WindowListMenuButton() {

        val root = tree(CMenuItem(null, "")) {

            child(CMenuItem(text = "New")) {
                child(CMenuItem(text = "Project"))
                child(CMenuItem(text = "Wallet"))
            }

            child(
                CMenuItem(
                    icon = painterResource("menu-open_dark.svg"),
                    text = "Open",
                    menuInterface = object : MenuInterface {
                        override fun onClick() {
                            WindowStateHolder.isOpenDialogOpen = true
                        }

                        override fun onHover(onEnter: Boolean) {

                        }

                    })
            )

            child(CMenuItem(
                text = "Switch Chain",
                menuInterface = object : MenuInterface {
                    override fun onClick() {
                        WindowStateHolder.isPreAvail = false
                        WindowStateHolder.isStoreWindowOpen = true
                    }

                    override fun onHover(onEnter: Boolean) {

                    }

                }
            ))

            child(CMenuItem(text = "Open Recent"))
            child(CMenuItem(text = "Settings"))
            child(CMenuItem(text = "Exit", menuInterface = object : MenuInterface {
                override fun onClick() {
                    WindowStateHolder.isMainWindowOpen = false
                }

                override fun onHover(onEnter: Boolean) {
                    TODO("Not yet implemented")
                }
            }))

        }

        Menu(MenuTree(root)) {

        }

    }

    @Composable
    override fun SwitchView() {

        fun onClick() {
            if (WindowStateHolder.view == View.EXPLORER) {
                WindowStateHolder.view = View.WALLET
            } else {
                WindowStateHolder.view = View.EXPLORER
            }
        }

        WindowButton(
            onClick = { onClick() },
            icon = painterResource("switchView_dark.svg"),
            contentDescription = "Switch View", width = 40.dp
        )
    }

    @Composable
    override fun ClusterMenu() {
        var expanded by remember { mutableStateOf(false) }
        val suggestions = listOf("Mainnet Beta", "Testnet", "Devnet", "Custom RPC URL")
        var setCluster by remember { mutableStateOf(suggestions[0]) }
        Box {
            Button(
                border = BorderStroke(1.dp, MaterialTheme.colors.surface),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Transparent,
                    contentColor = contentColorFor(MaterialTheme.colors.surface)
                ),
                onClick = { expanded = !expanded }) {
                Icon(
                    painterResource("clusterConnectedDot_dark.svg"),
                    contentDescription = "",
                    modifier = Modifier.align(Alignment.CenterVertically),
                    tint = Color.Green
                )
                Text(setCluster, fontSize = 11.sp)
                Icon(
                    imageVector = Icons.Filled.ArrowDropDown,
                    contentDescription = null,
                )
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.border(border = BorderStroke(2.dp, Color(0xFF4D5051)), shape = RectangleShape)
            ) {
                suggestions.forEach { label ->
                    DropdownMenuItem(modifier = Modifier.height(24.dp), onClick = {
                        if (label !== setCluster) {
                            setCluster = label
                        }
                        expanded = false

                    }) {
                        Text(text = label, fontSize = 12.sp)
                    }
                }
            }
        }
    }

    @Composable
    override fun TitleMenuBar(windowScope: WindowScope) {
        var restore by remember { mutableStateOf(true) }
        windowScope.TopBar(
            onClose = { WindowStateHolder.isMainWindowOpen = false },
            onRestore = {
                if (restore) {
                    WindowStateHolder.state.placement = WindowPlacement.Maximized
                    restore = false
                } else {
                    WindowStateHolder.state.placement = WindowPlacement.Floating
                    restore = true
                }
            },
            onMinimize = { WindowStateHolder.state.isMinimized = !WindowStateHolder.state.isMinimized },
            icon = painterResource("ic_launcher.png"),
            menuContent = {
                Box(
                    modifier = Modifier.offset(x = 5.dp).align(Alignment.CenterVertically)
                ) {
                    WindowListMenuButton()
                }
            },
            restoreIcon = if (restore) painterResource("maximize_dark.svg") else painterResource("restore_dark.svg"),
        ) {
            Text(
                "Cohesive  -",
                fontSize = 11.sp,
                modifier = Modifier.padding(end = 10.dp).align(Alignment.CenterVertically)
            )
            ClusterMenu()
            SwitchView()
        }
    }

    @Composable
    override fun ExplorerView() {
        Explorer()
    }

    @Composable
    override fun WalletView() {
        Wallet()
    }

    @Composable
    override fun EditorView() {
        Editor()
    }

    @Composable
    override fun CreateAccountDialog(
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
    ) = Dialog(
        onClose = onClose,
        text = text,
        negativeText = negativeText,
        neutralText = neutralText,
        positiveText = positiveText,
        onNegative = onNegative,
        onNeutral = onNeutral,
        onPositive = onPositive,
        negativeEnable = negativeEnable,
        neutralEnable = neutralEnable,
        positiveEnable = positiveEnable,
        width = width,
        height = height,
        content = content,
    )

    @Composable
    override fun ImportAccountDialog(
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
    ) = Dialog(
        onClose = onClose,
        text = text,
        negativeText = negativeText,
        neutralText = neutralText,
        positiveText = positiveText,
        onNegative = onNegative,
        onNeutral = onNeutral,
        onPositive = onPositive,
        negativeEnable = negativeEnable,
        neutralEnable = neutralEnable,
        positiveEnable = positiveEnable,
        width = width,
        height = height,
        content = content,
    )

    @Composable
    fun FileExplorerDialog() {
        Dialog(
            onClose = { WindowStateHolder.isOpenDialogOpen = !WindowStateHolder.isOpenDialogOpen },
            text = "Open File or Project",
            negativeText = "Cancel",
            onNegative = { WindowStateHolder.isOpenDialogOpen = !WindowStateHolder.isOpenDialogOpen },
            positiveText = "Ok",
            onPositive = {
                WindowStateHolder.view = View.EDITOR
                WindowStateHolder.isOpenDialogOpen = !WindowStateHolder.isOpenDialogOpen
            },
            width = 450.dp,
            height = 450.dp,
        ) {
            FileTree(model = FileTree(root = HomeFolder))
        }
    }

    @Composable
    fun Content() {
        with(LocalDensity.current) {
            if (WindowStateHolder.view == View.EXPLORER) {
                ExplorerView()
            } else if (WindowStateHolder.view == View.WALLET) {
                Wallet()
                if (WindowStateHolder.isImportAccountOpen) {
                    ImportAccountDialog(
                        onClose = { WindowStateHolder.isImportAccountOpen = !WindowStateHolder.isImportAccountOpen },
                        text = "Import Account",
                        negativeText = "Cancel",
                        onNegative = { WindowStateHolder.isImportAccountOpen = !WindowStateHolder.isImportAccountOpen },
                        neutralText = "",
                        onNeutral = {},
                        positiveText = "Import",
                        onPositive = {},
                        negativeEnable = true,
                        neutralEnable = false,
                        positiveEnable = true,
                        width = 400.dp,
                        height = 300.dp,
                    ) {
                        Text("Import Account")
                    }
                }

                if (WindowStateHolder.isCreateAccountOpen) {
                    CreateAccountDialog(
                        onClose = { WindowStateHolder.isCreateAccountOpen = !WindowStateHolder.isCreateAccountOpen },
                        text = "Create Account",
                        negativeText = "Cancel",
                        onNegative = { WindowStateHolder.isCreateAccountOpen = !WindowStateHolder.isCreateAccountOpen },
                        neutralText = "",
                        onNeutral = {},
                        positiveText = "Ok",
                        onPositive = {},
                        negativeEnable = true,
                        neutralEnable = false,
                        positiveEnable = true,
                        width = 400.dp,
                        height = 300.dp,
                    ) {
                        Text("Create Account")
                    }
                }

            } else {
                EditorView()
            }

            if (WindowStateHolder.isOpenDialogOpen) {
                FileExplorerDialog()
            }
        }
    }

    @Composable
    override fun Show(windowScope: WindowScope) {

        WindowScaffold(
            topBar = { TitleMenuBar(windowScope) },
        ) {
            Content()
        }

    }


}



