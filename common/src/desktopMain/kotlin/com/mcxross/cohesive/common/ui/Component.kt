package com.mcxross.cohesive.common.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun ClusterMenu() {
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
        ) {
            suggestions.forEach { label ->
                DropdownMenuItem(modifier = Modifier.height(20.dp), onClick = {
                    if (label !== setCluster) {
                        setCluster = label
                    }
                    expanded = false

                }) {
                    Text(text = label, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
fun CButton(text: String, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier.padding(8.dp),
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
    ) {
        Text(text = text)
    }
}

@Composable
fun TitleBar() {

    Column(modifier = Modifier.fillMaxWidth().height(25.dp)) {
        Box(modifier = Modifier.fillMaxSize()) {
            Row(modifier = Modifier.fillMaxSize()) {
                Text("Import Account", modifier = Modifier.align(Alignment.CenterVertically))
            }

            Box(
                modifier = Modifier.fillMaxHeight().width(54.dp).align(Alignment.CenterEnd)
            ) {
                DialogCloseButton()
            }

        }
        Divider()
    }
}
