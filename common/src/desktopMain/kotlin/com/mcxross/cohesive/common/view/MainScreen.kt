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
import com.mcxross.cohesive.common.ui.common.AppTheme
import com.mcxross.cohesive.common.ui.component.CMenu
import com.mcxross.cohesive.common.ui.component.CMenuItem
import com.mcxross.cohesive.common.ui.component.WindowButton
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

    val suggestions = listOf(
        CMenuItem(painterResource("menu-open_dark.svg"), "New"),
        CMenuItem(icon = null, "Open"),
        CMenuItem(icon = null, "Open Recent"),
        CMenuItem(icon = null, "Switch Chain"),
        CMenuItem(icon = null, "Settings"),
        CMenuItem(icon = null, "Exit"),
    )
    CMenu(suggestions)

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
                    }

                }

            }


        }

    }


}
