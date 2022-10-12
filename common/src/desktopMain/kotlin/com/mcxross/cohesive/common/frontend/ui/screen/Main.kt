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
import com.mcxross.cohesive.common.frontend.ui.view.wallet.Wallet
import com.mcxross.cohesive.common.frontend.ui.widget.Markdown
import com.mcxross.cohesive.common.frontend.ui.widget.MarkdownConfig
import com.mcxross.cohesive.common.frontend.utils.WindowState
import com.mcxross.cohesive.common.frontend.utils.isDirectory
import com.mcxross.cohesive.cps.Extension
import com.mcxross.cohesive.mellow.*

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
                            WindowState.isOpenDialogOpen = true
                        }

                        override fun onHover(onEnter: Boolean) {

                        }

                    })
            )

            child(CMenuItem(
                text = "Switch Chain",
                menuInterface = object : MenuInterface {
                    override fun onClick() {
                        WindowState.isPreAvail = false
                        WindowState.isStoreWindowOpen = true
                    }

                    override fun onHover(onEnter: Boolean) {

                    }

                }
            ))

            child(CMenuItem(text = "Open Recent"))
            child(CMenuItem(text = "Settings"))
            child(CMenuItem(text = "Exit", menuInterface = object : MenuInterface {
                override fun onClick() {
                    WindowState.isMainWindowOpen = false
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
            if (WindowState.view == View.EXPLORER) {
                WindowState.view = View.WALLET
            } else {
                WindowState.view = View.EXPLORER
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
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {

            var restore by remember { mutableStateOf(true) }
            Local.LocalContext.current.windowScope?.TopBar(
                onClose = { WindowState.isMainWindowOpen = false },
                onRestore = {
                    if (restore) {
                        WindowState.state.placement = WindowPlacement.Maximized
                        restore = false
                    } else {
                        WindowState.state.placement = WindowPlacement.Floating
                        restore = true
                    }
                },
                onMinimize = { WindowState.state.isMinimized = !WindowState.state.isMinimized },
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

            Divider()
        }

    }

    @Composable
    override fun ExplorerView() {
        //Explorer()
        Markdown(
            modifier = Modifier.fillMaxWidth().padding(10.dp),
            content = "## Table of contents\n" +
                    "- [Quick start](#quick-start)\n" +
                    "- [Status](#status)\n" +
                    "- [What's included](#whats-included)\n" +
                    "- [Documentation](#documentation)\n" +
                    "- [Features](#features)\n" +
                    "- [Roadmap](#roadmap)\n" +
                    "- [Design Philosophy](#design-philosophy)\n" +
                    "- [Contribution](#contribution)\n" +
                    "\n" +
                    "**Note:** This project is in its early stages of development. The project is not yet ready for production use.\n" +
                    "\n" +
                    "## Quick start\n" +
                    "> Convention: We'll use \"Platform\" and \"Blockchain\" interchangeably herein - and rarely, \"Chain\"\n" +
                    "\n" +
                    "Cohesive is a modular meta-tool, i.e., a tool for tools specifically for Blockchain technology. It's Multiplatform, and Blockchain-neutral with a base implementation of/and, core functionality including but not limited to UI framework, Plugin Store, Simple Editor, and IDE. Specific Platform implementation can be built and installed as a _primary_ plugin acting as the Platform wrapper, i.e., consuming Cohesive, and thus bending its gears for that specific Platform.\n" +
                    "\n" +
                    "### Running Base desktop application",
            config = MarkdownConfig(
                isLinksClickable = true,
                isImagesClickable = true,
                isScrollEnabled = true
            )
        ) { link, type ->

        }
    }

    @Composable
    override fun WalletView() {
        Wallet()
    }

    @Composable
    override fun EditorView() {
        if ((WindowState.currentProjectFile as File).isDirectory) {
            EditorComposite(
                file = WindowState.currentProjectFile as File,
            )
        } else {
            EditorSimple(
                file = WindowState.currentProjectFile as File,
            )
        }
    }

    @Composable
    fun FileExplorerDialog() {
        var file by remember { mutableStateOf(HomeFolder) }
        Dialog(
            onClose = { WindowState.isOpenDialogOpen = !WindowState.isOpenDialogOpen },
            text = "Open File or Project",
            negativeText = "Cancel",
            onNegative = { WindowState.isOpenDialogOpen = !WindowState.isOpenDialogOpen },
            positiveText = "Ok",
            onPositive = {
                WindowState.currentProjectFile = file
                WindowState.view = View.EDITOR
                WindowState.isOpenDialogOpen = !WindowState.isOpenDialogOpen
            },
            width = 450.dp,
            height = 450.dp,
        ) {
            FileTree(model = FileTreeModel(root = HomeFolder)) {
                file = it
            }
        }
    }

    @Composable
    fun Content() {
        with(LocalDensity.current) {
            Crossfade(WindowState.view) { view ->
                when (view) {
                    View.EXPLORER -> ExplorerView()
                    View.WALLET -> WalletView()
                    View.EDITOR -> EditorView()
                }
            }

            if (WindowState.isOpenDialogOpen) {
                FileExplorerDialog()
            }
        }
    }

    @Composable
    override fun Compose() {
        WindowScaffold(
            topBar = { TitleMenuBar() },
            onDragStarted = { uris, _ ->
                uris.isNotEmpty()
            },
            onDragEntered = { },
            onDropped = { uris, _ ->
                if (isDirectory(uris[0].path)) {
                    WindowState.currentProjectFile = uris[0].path
                    WindowState.view = View.EDITOR
                }
                true
            },
        ) {
            Content()
        }
    }


}



