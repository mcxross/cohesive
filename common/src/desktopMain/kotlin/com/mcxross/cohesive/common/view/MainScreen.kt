package com.mcxross.cohesive.common.view

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import com.mcxross.cohesive.common.ui.CreateAccountDialog
import com.mcxross.cohesive.common.ui.ImportAccountDialog
import com.mcxross.cohesive.common.ui.OpenDialog
import com.mcxross.cohesive.common.ui.common.AppTheme
import com.mcxross.cohesive.common.ui.component.*
import com.mcxross.cohesive.common.utils.WindowStateHolder

@Composable
private fun WindowMinimizeButton() {

    WindowButton("minimize_dark.svg", "Minimize window") {
        WindowStateHolder.state.isMinimized = !WindowStateHolder.state.isMinimized
    }
}

@Composable
private fun WindowResizeButton() {
    var maximize by remember { mutableStateOf(true) }
    WindowButton(if (maximize) "maximize_dark.svg" else "restore_dark.svg", "Restore Down, and Maximize window") {
        if (maximize) {
            WindowStateHolder.state.placement = WindowPlacement.Maximized
            maximize = false
        } else {
            WindowStateHolder.state.placement = WindowPlacement.Floating
            maximize = true
        }
    }
}

@Composable
private fun WindowCloseButton() {
    WindowButton("close_dark.svg", "Close window", onHoverColor = Color.Red) {
        WindowStateHolder.isWindowOpen = false
    }
}

@Composable
private fun WindowButtons() {
    Row(modifier = Modifier.fillMaxSize()) {
        WindowMinimizeButton()
        WindowResizeButton()
        WindowCloseButton()
    }
}

@Composable
private fun WindowListMenuButton() {

    val rootMenu = CMenuItem(null, "").addMenu(
        CMenuItem(
            icon = null,
            text = "New",
            children = listOf(
                CMenuItem(icon = null, text = "Project"),
                CMenuItem(icon = null, text = "Wallet")
            )
        )
    ).addMenu(
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
    ).addMenu(CMenuItem(icon = null, "Open Recent"))
        .addMenu(CMenuItem(icon = null, "Settings"))
        .addMenu(CMenuItem(icon = null, "Exit", cMenuInterface = object : CMenuInterface {
            override fun onClick() {
                WindowStateHolder.isWindowOpen = false
            }

            override fun onHover(onEnter: Boolean) {
                TODO("Not yet implemented")
            }
        }))
    CMenu(MenuTree(rootMenu))

}

@Composable
private fun SwitchView() {

    WindowButton("switchView_dark.svg", contentDescription = "Switch View", width = 40.dp) {

        if (WindowStateHolder.view == View.EXPLORER) {
            WindowStateHolder.view = View.WALLET
        } else {
            WindowStateHolder.view = View.EXPLORER
        }

    }
}


@Composable
private fun ClusterMenu() {
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
            Text(setCluster, fontSize = 12.sp)
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
private fun WindowScope.TitleMenuBar() {
    WindowDraggableArea {
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
                        "Solana  -",
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
fun WindowScope.MainScreen() {
    DisableSelection {
        MaterialTheme(colors = AppTheme.getColors()) {

            Surface(
                modifier = Modifier.fillMaxSize(),
                contentColor = contentColorFor(MaterialTheme.colors.surface)
            ) {
                Column {
                    TitleMenuBar()

                    Divider()

                    with(LocalDensity.current) {
                        if (WindowStateHolder.view == View.EXPLORER) {
                            ExplorerView()
                        } else {
                            WalletView()
                            if (WindowStateHolder.isImportAccountOpen) {
                                ImportAccountDialog()
                            }

                            if (WindowStateHolder.isCreateAccountOpen) {
                                CreateAccountDialog()
                            }

                        }

                        if(WindowStateHolder.isOpenDialogOpen) {
                            OpenDialog()
                        }
                    }

                }

            }


        }

    }


}
