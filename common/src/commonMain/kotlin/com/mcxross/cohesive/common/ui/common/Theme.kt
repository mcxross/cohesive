package com.mcxross.cohesive.common.ui.common

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AppTheme {

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
        val backgroundDark: Color = Color(0xFF2B2B2B)
        val backgroundDarkMedium: Color = Color(0xFF3C3F41)
        val backgroundDarkLight: Color = Color(0xFF4E5254)

        val materialDark: androidx.compose.material.Colors = darkColors(
            background = backgroundDark,
            surface = backgroundDarkMedium,
            primary = Color.White,
        )

        val backgroundWhite: Color = Color(0xFFFFFFFF)
        val backgroundWhiteMedium: Color = Color(0xFFF2F2F2)

        val materialWhite: androidx.compose.material.Colors = lightColors(
            background = backgroundWhite,
            surface = backgroundWhiteMedium,
            primary = Color.Black,
        )

    }
}