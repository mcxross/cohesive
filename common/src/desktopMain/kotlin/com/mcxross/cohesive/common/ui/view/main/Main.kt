package com.mcxross.cohesive.common.ui.view.main

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.selection.DisableSelection
import androidx.compose.foundation.window.WindowDraggableArea
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.ds.tree.tree
import com.mcxross.cohesive.common.openapi.ui.view.IMain
import com.mcxross.cohesive.common.ui.component.*
import com.mcxross.cohesive.common.ui.dialog.CreateAccountDialog
import com.mcxross.cohesive.common.ui.dialog.ImportAccountDialog
import com.mcxross.cohesive.common.ui.dialog.OpenDialog
import com.mcxross.cohesive.common.ui.theme.AppTheme
import com.mcxross.cohesive.common.ui.view.View
import com.mcxross.cohesive.common.ui.view.editor.CodeViewer
import com.mcxross.cohesive.common.ui.view.editor.CodeViewerView
import com.mcxross.cohesive.common.ui.view.editor.Editors
import com.mcxross.cohesive.common.ui.view.editor.Settings
import com.mcxross.cohesive.common.ui.view.editor.filetree.FileTree
import com.mcxross.cohesive.common.ui.view.editor.platform.HomeFolder
import com.mcxross.cohesive.common.ui.view.explorer.Explorer
import com.mcxross.cohesive.common.ui.view.wallet.Wallet
import com.mcxross.cohesive.common.utils.WindowStateHolder
import org.pf4j.Extension

@Extension
open class Main : IMain {

    @Composable
    override fun WindowButtons() {
        var maximize by remember { mutableStateOf(true) }
        Row(modifier = Modifier.fillMaxSize()) {
            WindowButton("minimize_dark.svg", "Minimize window") {
                WindowStateHolder.state.isMinimized = !WindowStateHolder.state.isMinimized
            }
            WindowButton(
                if (maximize) "maximize_dark.svg" else "restore_dark.svg",
                "Restore Down, and Maximize window"
            ) {
                if (maximize) {
                    WindowStateHolder.state.placement = WindowPlacement.Maximized
                    maximize = false
                } else {
                    WindowStateHolder.state.placement = WindowPlacement.Floating
                    maximize = true
                }
            }
            WindowButton("close_dark.svg", "Close window", onHoverColor = Color.Red) {
                WindowStateHolder.isMainWindowOpen = false
            }
        }
    }

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
                    cMenuInterface = object : CMenuInterface {
                        override fun onClick() {
                            WindowStateHolder.isOpenDialogOpen = true
                        }

                        override fun onHover(onEnter: Boolean) {

                        }

                    })
            )

            child(CMenuItem(
                text = "Switch Chain",
                cMenuInterface = object : CMenuInterface {
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
            child(CMenuItem(text = "Exit", cMenuInterface = object : CMenuInterface {
                override fun onClick() {
                    WindowStateHolder.isMainWindowOpen = false
                }

                override fun onHover(onEnter: Boolean) {
                    TODO("Not yet implemented")
                }
            }))

        }

        CMenu(MenuTree(root)) {

        }

    }

    @Composable
    override fun SwitchView() {

        WindowButton("switchView_dark.svg", contentDescription = "Switch View", width = 40.dp) {

            if (WindowStateHolder.view == View.EXPLORER) {
                WindowStateHolder.view = View.WALLET
            } else {
                WindowStateHolder.view = View.EXPLORER
            }

        }
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
        windowScope.WindowDraggableArea {
            Column(modifier = Modifier.fillMaxWidth().height(30.dp)) {

                Box(modifier = Modifier.fillMaxSize().padding(start = 10.dp)) {
                    Row(modifier = Modifier.fillMaxSize()) {
                        Image(
                            painterResource("ic_launcher.png"),
                            "App Icon",
                            modifier = Modifier.size(25.dp).align(Alignment.CenterVertically)
                        )

                        Box(modifier = Modifier.fillMaxSize().offset(x = 5.dp).align(Alignment.CenterVertically)) {

                            WindowListMenuButton()

                        }

                    }

                    Row(modifier = Modifier.align(Alignment.Center)) {
                        Text(
                            "Cohesive  -",
                            fontSize = 11.sp,
                            modifier = Modifier.padding(end = 10.dp).align(Alignment.CenterVertically)
                        )
                        ClusterMenu()
                        SwitchView()
                    }

                    Box(
                        modifier = Modifier.fillMaxHeight().width(162.dp).align(Alignment.CenterEnd)
                    ) {
                        WindowButtons()
                    }
                }

            }
        }
    }

    @Composable
    override fun Show(windowScope: WindowScope) {
        DisableSelection {
            AppTheme.Theme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    contentColor = contentColorFor(MaterialTheme.colors.surface)
                ) {
                    Column {
                        TitleMenuBar(windowScope)

                        Divider()

                        with(LocalDensity.current) {
                            if (WindowStateHolder.view == View.EXPLORER) {
                                Explorer()
                            } else if (WindowStateHolder.view == View.WALLET) {
                                Wallet()
                                if (WindowStateHolder.isImportAccountOpen) {
                                    ImportAccountDialog()
                                }

                                if (WindowStateHolder.isCreateAccountOpen) {
                                    CreateAccountDialog()
                                }

                            } else {

                                val codeViewer = remember {
                                    val editors = Editors()

                                    CodeViewer(
                                        editors = editors,
                                        fileTree = FileTree(HomeFolder),
                                        settings = Settings()
                                    )
                                }

                                CodeViewerView(codeViewer)
                            }

                            if (WindowStateHolder.isOpenDialogOpen) {
                                OpenDialog()
                            }
                        }

                    }

                }

            }

        }

    }


}



