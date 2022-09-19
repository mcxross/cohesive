package com.mcxross.cohesive.mellow

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "",
    contentDescription: String? = text,
    icon: Painter? = null,
    iconTint: Color = MaterialTheme.colors.onSurface,
    iconModifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = RoundedCornerShape(12.dp),
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable (RowScope.() -> Unit)? = null,
) = Button(
    onClick = onClick,
    modifier = modifier.padding(2.dp),
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = shape,
    border = border,
    colors = colors,
    contentPadding = contentPadding,
) {
    if (content == null) {
        if (icon != null) {
            Icon(
                painter = icon,
                contentDescription = contentDescription,
                tint = iconTint,
                modifier = iconModifier
            )
        }
        if (text.isNotEmpty()) {
            Text(
                text = text,
                fontWeight = FontWeight.W300,
                color = if (enabled) Color.White else contentColorFor(MaterialTheme.colors.surface)
            )
        }
    } else {
        content()
    }

}


@Composable
fun OutlinedButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    text: String = "",
    contentDescription: String? = text,
    icon: Painter? = null,
    iconTint: Color = MaterialTheme.colors.onSurface,
    iconModifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.elevation(),
    shape: Shape = RoundedCornerShape(12.dp),
    border: BorderStroke? = ButtonDefaults.outlinedBorder,
    colors: ButtonColors = ButtonDefaults.outlinedButtonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable (RowScope.() -> Unit)? = null,
) = Button(
    onClick = onClick,
    modifier = modifier,
    text = text,
    icon = icon,
    contentDescription = contentDescription,
    iconTint = iconTint,
    iconModifier = iconModifier,
    enabled = enabled,
    interactionSource = interactionSource,
    elevation = elevation,
    shape = shape,
    border = border,
    colors = colors,
    contentPadding = contentPadding,
    content = content,
)