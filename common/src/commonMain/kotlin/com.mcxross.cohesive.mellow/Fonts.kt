package com.mcxross.cohesive.mellow

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight

object Fonts {
  @Composable
  fun rubikFont() =
    FontFamily(
      Font(
        "Rubik",
        res = "rubik_light",
        FontWeight.W300,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        res = "rubik_light_italic",
        FontWeight.W300,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        res = "rubik_regular",
        FontWeight.W400,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        res = "rubik_italic",
        FontWeight.W400,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        res = "rubik_medium",
        FontWeight.W500,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        res = "rubik_medium_italic",
        FontWeight.W500,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        res = "rubik_semi_bold",
        FontWeight.W600,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        res = "rubik_semi_bold_italic",
        FontWeight.W600,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        res = "rubik_bold",
        FontWeight.W700,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        res = "rubik_bold_italic",
        FontWeight.W700,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        res = "rubik_extra_bold",
        FontWeight.W800,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        res = "rubik_extra_bold_italic",
        FontWeight.W800,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        res = "rubik_black",
        FontWeight.W900,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        res = "rubik_black_italic",
        FontWeight.W900,
        FontStyle.Italic,
      ),
    )

  @Composable
  fun rubikFont1() =
    FontFamily(
      Font(
        "Rubik",
        "rubik_black",
        FontWeight.Black,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        "rubik_black_italic",
        FontWeight.Black,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        "rubik_light",
        FontWeight.Light,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        "rubik_light_italic",
        FontWeight.Light,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        "rubik_regular",
        FontWeight.Normal,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        "rubik_italic",
        FontWeight.Normal,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        "rubik_medium",
        FontWeight.Medium,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        "rubik_medium_italic",
        FontWeight.Medium,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        "rubik_bold",
        FontWeight.Bold,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        "rubik_bold_italic",
        FontWeight.Bold,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        "rubik_semi_bold",
        FontWeight.SemiBold,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        "rubik_semi_bold_italic",
        FontWeight.SemiBold,
        FontStyle.Italic,
      ),
      Font(
        "Rubik",
        "rubik_extra_bold",
        FontWeight.ExtraBold,
        FontStyle.Normal,
      ),
      Font(
        "Rubik",
        "rubik_extra_bold_italic",
        FontWeight.ExtraBold,
        FontStyle.Italic,
      ),
    )

  @Composable
  fun jetbrainsMono() =
    FontFamily(
      Font(
        "JetBrains Mono",
        "jetbrainsmono_regular",
        FontWeight.Normal,
        FontStyle.Normal,
      ),
      Font(
        "JetBrains Mono",
        "jetbrainsmono_italic",
        FontWeight.Normal,
        FontStyle.Italic,
      ),
      Font(
        "JetBrains Mono",
        "jetbrainsmono_bold",
        FontWeight.Bold,
        FontStyle.Normal,
      ),
      Font(
        "JetBrains Mono",
        "jetbrainsmono_bold_italic",
        FontWeight.Bold,
        FontStyle.Italic,
      ),
      Font(
        "JetBrains Mono",
        "jetbrainsmono_extrabold",
        FontWeight.ExtraBold,
        FontStyle.Normal,
      ),
      Font(
        "JetBrains Mono",
        "jetbrainsmono_extrabold_italic",
        FontWeight.ExtraBold,
        FontStyle.Italic,
      ),
      Font(
        "JetBrains Mono",
        "jetbrainsmono_medium",
        FontWeight.Medium,
        FontStyle.Normal,
      ),
      Font(
        "JetBrains Mono",
        "jetbrainsmono_medium_italic",
        FontWeight.Medium,
        FontStyle.Italic,
      ),
    )
}
