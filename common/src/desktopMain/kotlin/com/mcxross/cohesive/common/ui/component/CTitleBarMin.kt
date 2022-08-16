package com.mcxross.cohesive.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
private fun CloseButton(onClose: () -> Unit) {
    WindowButton(
        "close_dark.svg",
        contentDescription = "Close Dialog",
        onHoverColor = Color(0xFFDB5860)
    ) {
        onClose()
    }
}


@Composable
fun TitleBar(title: String, modifier: Modifier, onClose: () -> Unit) {
    Column(modifier = modifier.fillMaxWidth().height(30.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(29.5.dp).padding(start = 8.dp)) {
            Row(modifier = Modifier.fillMaxSize()) {
                Image(
                    painterResource("ic_launcher.png"),
                    "App Icon",
                    modifier = Modifier.size(25.dp).align(Alignment.CenterVertically)
                )
                Text(
                    text = title,
                    fontSize = 12.sp,
                    maxLines = 1,
                    modifier = Modifier.offset(x = 5.dp).align(Alignment.CenterVertically)
                )
            }

            Box(
                modifier = Modifier.fillMaxHeight().width(54.dp).align(Alignment.CenterEnd)
            ) {
                CloseButton(onClose)
            }

        }
        Divider(thickness = 0.5.dp)
    }
}