package com.mcxross.cohesive.mellow.icon

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.DefaultFillType
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.PathBuilder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

object Icons {

  object Brand

  object Filled

  object Regular

}

/**
 * Utility delegate to construct a Mellow icon with default size information.
 * This is used by generated icons, and should not be used manually.
 *
 * @param name the full name of the generated icon
 * @param block builder lambda to add paths to this vector asset
 */
inline fun mellowIcon(
  name: String,
  block: ImageVector.Builder.() -> ImageVector.Builder
): ImageVector = ImageVector.Builder(
  name = name,
  defaultWidth = MaterialIconDimension.dp,
  defaultHeight = MaterialIconDimension.dp,
  viewportWidth = MaterialIconDimension,
  viewportHeight = MaterialIconDimension,
).block().build()

/**
 * Adds a vector path to this icon with Mellow defaults.
 *
 * @param fillAlpha fill alpha for this path
 * @param strokeAlpha stroke alpha for this path
 * @param pathFillType [PathFillType] for this path
 * @param pathBuilder builder lambda to add commands to this path
 */
inline fun ImageVector.Builder.mellowPath(
  fillAlpha: Float = 1f,
  strokeAlpha: Float = 1f,
  pathFillType: PathFillType = DefaultFillType,
  pathBuilder: PathBuilder.() -> Unit
) =
  path(
    fill = SolidColor(Color.Black),
    fillAlpha = fillAlpha,
    stroke = null,
    strokeAlpha = strokeAlpha,
    strokeLineWidth = 1f,
    strokeLineCap = StrokeCap.Butt,
    strokeLineJoin = StrokeJoin.Bevel,
    strokeLineMiter = 1f,
    pathFillType = pathFillType,
    pathBuilder = pathBuilder,
  )

// All Mellow icons (currently) are 16dp by 16dp, with a viewport size of 16 by 16.
@PublishedApi
internal const val MaterialIconDimension = 16f
