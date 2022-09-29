package com.mcxross.cohesive.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mcxross.cohesive.mellow.Button
import com.mcxross.cohesive.mellow.VerticalScrollbar
import kotlinx.coroutines.launch

@Preview
@Composable
fun AppPreview() {
    App()
}

@Preview
@Composable
fun Button() {
    Button({}, modifier = Modifier.height(35.dp), text = "Ok") {}
}