package com.mcxross.cohesive.common.ui.theme

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.mcxross.cohesive.common.ui.view.editor.platform.Font

object Fonts {
    @Composable
    fun rubikFont() = FontFamily(
        Font(
            "Rubik",
            res = "Rubik-Light",
            FontWeight.W300,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            res = "Rubik-LightItalic",
            FontWeight.W300,
            FontStyle.Italic
        ),
        Font(
            "Rubik",
            res = "Rubik-Regular",
            FontWeight.W400,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            res = "Rubik-Italic",
            FontWeight.W400,
            FontStyle.Italic
        ),
        Font(
            "Rubik",
            res = "Rubik-Medium",
            FontWeight.W500,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            res = "Rubik-MediumItalic",
            FontWeight.W500,
            FontStyle.Italic
        ),
        Font(
            "Rubik",
            res = "Rubik-SemiBold",
            FontWeight.W600,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            res = "Rubik-SemiBoldItalic",
            FontWeight.W600,
            FontStyle.Italic
        ),
        Font(
            "Rubik",
            res = "Rubik-Bold",
            FontWeight.W700,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            res = "Rubik-BoldItalic",
            FontWeight.W700,
            FontStyle.Italic
        ),
        Font(
            "Rubik",
            res = "Rubik-ExtraBold",
            FontWeight.W800,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            res = "Rubik-ExtraBoldItalic",
            FontWeight.W800,
            FontStyle.Italic
        ),
        Font(
            "Rubik",
            res = "Rubik-Black",
            FontWeight.W900,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            res = "Rubik-BlackItalic",
            FontWeight.W900,
            FontStyle.Italic
        )

    )

    @Composable
    fun rubikFont1() = FontFamily (
        Font(
            "Rubik",
            "Rubik-Black",
            FontWeight.Black,
            FontStyle.Normal,
        ),
        Font(
            "Rubik",
            "Rubik-BlackItalic",
            FontWeight.Black,
            FontStyle.Italic,
        ),
        Font(
            "Rubik",
            "Rubik-Light",
            FontWeight.Light,
            FontStyle.Normal,
        ),
        Font(
            "Rubik",
            "Rubik-LightItalic",
            FontWeight.Light,
            FontStyle.Italic,
        ),
        Font(
            "Rubik",
            "Rubik-Regular",
            FontWeight.Normal,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            "Rubik-Italic",
            FontWeight.Normal,
            FontStyle.Italic
        ),
        Font(
            "Rubik",
            "Rubik-Medium",
            FontWeight.Medium,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            "Rubik-MediumItalic",
            FontWeight.Medium,
            FontStyle.Italic
        ),
        Font(
            "Rubik",
            "Rubik-Bold",
            FontWeight.Bold,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            "Rubik-BoldItalic",
            FontWeight.Bold,
            FontStyle.Italic
        ),
        Font(
            "Rubik",
            "Rubik-SemiBold",
            FontWeight.SemiBold,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            "Rubik-SemiBoldItalic",
            FontWeight.SemiBold,
            FontStyle.Italic
        ),
        Font(
            "Rubik",
            "Rubik-ExtraBold",
            FontWeight.ExtraBold,
            FontStyle.Normal
        ),
        Font(
            "Rubik",
            "Rubik-ExtraBoldItalic",
            FontWeight.ExtraBold,
            FontStyle.Italic
        ),
    )

    @Composable
    fun jetbrainsMono() = FontFamily(
        Font(
            "JetBrains Mono",
            "jetbrainsmono_regular",
            FontWeight.Normal,
            FontStyle.Normal
        ),
        Font(
            "JetBrains Mono",
            "jetbrainsmono_italic",
            FontWeight.Normal,
            FontStyle.Italic
        ),

        Font(
            "JetBrains Mono",
            "jetbrainsmono_bold",
            FontWeight.Bold,
            FontStyle.Normal
        ),
        Font(
            "JetBrains Mono",
            "jetbrainsmono_bold_italic",
            FontWeight.Bold,
            FontStyle.Italic
        ),

        Font(
            "JetBrains Mono",
            "jetbrainsmono_extrabold",
            FontWeight.ExtraBold,
            FontStyle.Normal
        ),
        Font(
            "JetBrains Mono",
            "jetbrainsmono_extrabold_italic",
            FontWeight.ExtraBold,
            FontStyle.Italic
        ),

        Font(
            "JetBrains Mono",
            "jetbrainsmono_medium",
            FontWeight.Medium,
            FontStyle.Normal
        ),
        Font(
            "JetBrains Mono",
            "jetbrainsmono_medium_italic",
            FontWeight.Medium,
            FontStyle.Italic
        )
    )
}