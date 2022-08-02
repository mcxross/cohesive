package com.mcxross.cohesive.common.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import com.mcxross.cohesive.common.ui.common.component.CButton

private var titleStr: String = ""
private var negativeTextStr: String = ""
private var neutralTextStr: String = ""
private var positiveTextStr: String = ""
private var onNegativeFun: () -> Unit = {}
private var onNeutralFun: () -> Unit = {}
private var onPositiveFun: () -> Unit = {}
private var onCloseFun: () -> Unit = {}

@Composable
private fun DialogCloseButton() {
    WindowButton(
        "closeSmall_dark.svg",
        contentDescription = "Close Dialog",
        onHoverColor = Color(0xFFDB5860),
        tint = Color.White
    ) {
        onCloseFun()
    }
}

@Composable
private fun Title(modifier: Modifier) {
    Column(modifier = modifier.fillMaxWidth().height(30.dp)) {
        Box(modifier = Modifier.fillMaxWidth().height(29.5.dp).padding(start = 8.dp)) {
            Row(modifier = Modifier.fillMaxSize()) {
                Image(
                    painterResource("ic_launcher.png"),
                    "App Icon",
                    modifier = Modifier.size(25.dp).align(Alignment.CenterVertically)
                )
                Text(text = titleStr, fontSize = 12.sp, maxLines = 1, modifier = Modifier.offset(x = 5.dp).align(Alignment.CenterVertically))
            }

            Box(
                modifier = Modifier.fillMaxHeight().width(54.dp).align(Alignment.CenterEnd)
            ) {
                DialogCloseButton()
            }

        }
        Divider(thickness = 0.5.dp)
    }
}

@Composable
private fun Body(modifier: Modifier, content: @Composable () -> Unit) {
    Box(modifier = modifier) {
        content()
    }
}

@Composable
private fun Footer(modifier: Modifier) {
    Column(modifier = modifier) {
        Divider()
        Box(modifier = Modifier.fillMaxWidth().height(55.dp).padding(end = 5.dp)) {
            Row(modifier = Modifier.fillMaxHeight().align(Alignment.CenterEnd)) {
                CButton(
                    text = positiveTextStr,
                    enabled = false,
                    modifier = Modifier.height(35.dp).align(Alignment.CenterVertically)
                ) {
                    onPositiveFun()
                }
                CButton(negativeTextStr, modifier = Modifier.height(35.dp).align(Alignment.CenterVertically)) {
                    onNegativeFun()
                }
            }
        }
    }
}

@Composable
fun CDialog(
    title: String,
    width: Dp = 400.dp,
    height: Dp = 300.dp,
    negativeText: String = "",
    neutralText: String = "",
    positiveText: String = "",
    onNegative: () -> Unit = {},
    onNeutral: () -> Unit = {},
    onPositive: () -> Unit = {},
    onClose: () -> Unit,
    content: @Composable() () -> Unit
) {
    titleStr = title
    negativeTextStr = negativeText
    neutralTextStr = neutralText
    positiveTextStr = positiveText
    onNegativeFun = onNegative
    onNeutralFun = onNeutral
    onPositiveFun = onPositive
    onCloseFun = onClose
    var isDialogOpen by remember { mutableStateOf(false) }
    Dialog(
        onCloseRequest = { isDialogOpen = false },
        undecorated = true,
        resizable = false,
        state = rememberDialogState(position = WindowPosition(Alignment.Center), width = width, height = height),
    ) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            contentColor = contentColorFor(MaterialTheme.colors.surface)
        ) {

            Box(modifier = Modifier.fillMaxSize()) {
                Body(Modifier.matchParentSize().align(Alignment.Center).padding(top = 30.dp, bottom = 57.dp), content)
                Title(modifier = Modifier.align(Alignment.TopStart))
                Footer(Modifier.fillMaxWidth().align(Alignment.BottomStart))
            }

        }
    }

}