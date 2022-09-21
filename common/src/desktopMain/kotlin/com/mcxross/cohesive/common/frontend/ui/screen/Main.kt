package com.mcxross.cohesive.common.frontend.ui.screen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import com.mcxross.cohesive.common.ds.tree.tree
import com.mcxross.cohesive.common.frontend.model.Local
import com.mcxross.cohesive.common.frontend.openapi.ui.screen.IMain
import com.mcxross.cohesive.common.frontend.ui.view.View
import com.mcxross.cohesive.common.frontend.ui.view.explorer.Explorer
import com.mcxross.cohesive.common.frontend.ui.view.wallet.Wallet
import com.mcxross.cohesive.common.frontend.utils.WindowStateHolder
import com.mcxross.cohesive.common.frontend.utils.isDirectory
import com.mcxross.cohesive.common.utils.Log
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
        val suggestions = Local.LocalContext.current.environment!!.chains
        var setCluster by remember { mutableStateOf(suggestions[0]) }
        Box {
            Button(
                modifier = Modifier.defaultMinSize(minWidth = 150.dp),
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
                modifier = Modifier.defaultMinSize(minWidth = 150.dp)
                    .border(border = BorderStroke(2.dp, Color(0xFF4D5051)), shape = RectangleShape)
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
    override fun TitleMenuBar() {
        var restore by remember { mutableStateOf(true) }
        Local.LocalContext.current.windowScope?.TopBar(
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
        EditorComposite()
    }

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
            FileTree(FileTree(HomeFolder))
        }
    }

    @Composable
    fun Content() {
        with(LocalDensity.current) {
            Crossfade(WindowStateHolder.view) { view ->
                when (view) {
                    View.EXPLORER -> ExplorerView()
                    View.WALLET -> WalletView()
                    View.EDITOR -> EditorView()
                }
            }

            if (WindowStateHolder.isOpenDialogOpen) {
                FileExplorerDialog()
            }
        }
    }

    @Composable
    override fun Compose() {
        WindowScaffold(
            topBar = { TitleMenuBar() },
            onDragStarted = { _, _ ->
                true
            },
            onDropped = { uris, _ ->
                uris.size == 1
            },
        ) {
            Content()
        }
    }


}



