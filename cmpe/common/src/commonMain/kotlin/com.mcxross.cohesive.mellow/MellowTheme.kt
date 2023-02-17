package com.mcxross.cohesive.mellow

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle

object MellowTheme {

  val code: Code = Code()

  @Composable
  fun getColors(): androidx.compose.material.Colors {
    val colors = Colors()
    return if (isSystemInDarkTheme()) {
      colors.materialDark
    } else {
      colors.materialWhite
    }
  }

  class Colors {
    private val primaryDark: Color = Color(0xFF0C68BD)
    private val primaryVariantDark: Color = Color(0xFF1277CF)
    private val backgroundDark: Color = Color(0xFF2B2B2B)
    private val onBackgroundDark: Color = Color(0xFFA9A9A9)
    val backgroundDarkLight: Color = Color(0xFF4E5254)
    private val surfaceDark: Color = Color(0xFF3C3F41)
    private val onSurfaceDark: Color = Color(0xFFA9A9A9)

    val materialDark: androidx.compose.material.Colors =
      darkColors(
        primary = primaryDark,
        primaryVariant = primaryVariantDark,
        background = backgroundDark,
        onBackground = onBackgroundDark,
        surface = surfaceDark,
        onSurface = onSurfaceDark,
      )

    val backgroundWhite: Color = Color(0xFFFFFFFF)
    val backgroundWhiteMedium: Color = Color(0xFFF2F2F2)

    val materialWhite: androidx.compose.material.Colors =
      lightColors(
        background = backgroundWhite,
        surface = backgroundWhiteMedium,
        primary = Color.Black,
      )
  }

  class Code(
    val simple: SpanStyle = SpanStyle(Color(0xFFA9B7C6)),
    val value: SpanStyle = SpanStyle(Color(0xFF6897BB)),
    val keyword: SpanStyle = SpanStyle(Color(0xFFCC7832)),
    val punctuation: SpanStyle = SpanStyle(Color(0xFFA1C17E)),
    val annotation: SpanStyle = SpanStyle(Color(0xFFBBB529)),
    val comment: SpanStyle = SpanStyle(Color(0xFF808080)),
  )

  @Composable
  fun Theme(content: @Composable () -> Unit) {
    androidx.compose.material.MaterialTheme(
      colors = getColors(),
      typography = Typography(defaultFontFamily = Fonts.rubikFont()),
    ) {
      content()
    }
  }
}
