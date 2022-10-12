package com.mcxross.cohesive.common.frontend.ui.widget.md.markup

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.mcxross.cohesive.common.frontend.ui.widget.MarkdownKeysManager

@OptIn(ExperimentalUnitApi::class)
@Composable
fun MKStyledText(
    text: String,
    layer: String,
    textSize: Float? = null,
    color: Color,
) {
    Text(
        color = color,
        text = text,
        modifier = Modifier.padding(5.dp),
        fontSize = TextUnit(textSize ?: getFontSize(layer), TextUnitType.Sp)
    )
}

fun getFontSize(layer: String): Float {
    return when (layer) {
        MarkdownKeysManager.TEXT_HASH -> 17f
        MarkdownKeysManager.TEXT_HASH_2 -> 15f
        MarkdownKeysManager.TEXT_HASH_3 -> 13f
        MarkdownKeysManager.TEXT_HASH_4 -> 11f
        MarkdownKeysManager.TEXT_HASH_5 -> 9f
        MarkdownKeysManager.TEXT_HASH_6 -> 7f
        else -> 17f
    }
}

/**
 * h1, h2, h3, h4, h5, h6
 */
data class MarkdownStyledTextComponent(
    val text: String,
    val layer: String,
) : Element
