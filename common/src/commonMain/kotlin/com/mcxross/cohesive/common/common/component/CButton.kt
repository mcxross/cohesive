package com.mcxross.cohesive.common.common.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun CButton(text: String, enabled: Boolean = true, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        modifier = modifier.padding(2.dp),
        shape = RoundedCornerShape(12.dp),
        enabled = enabled,
        onClick = onClick,
    ) {
        Text(text = text, color = if (enabled) Color.White else contentColorFor(MaterialTheme.colors.surface))
    }
}