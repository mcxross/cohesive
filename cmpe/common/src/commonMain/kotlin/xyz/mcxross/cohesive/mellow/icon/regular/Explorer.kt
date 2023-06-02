package xyz.mcxross.cohesive.mellow.icon.regular

import androidx.compose.ui.graphics.vector.ImageVector
import xyz.mcxross.cohesive.mellow.icon.Icons
import xyz.mcxross.cohesive.mellow.icon.mellowIcon
import xyz.mcxross.cohesive.mellow.icon.mellowPath

public val Icons.Regular.Explorer: ImageVector
  get() {
    if (xyz.mcxross.cohesive.mellow.icon.regular._explorer != null) {
      return xyz.mcxross.cohesive.mellow.icon.regular._explorer!!
    }
    xyz.mcxross.cohesive.mellow.icon.regular._explorer = mellowIcon(name = "Regular.Explorer") {

      mellowPath {

      }

    }
    return xyz.mcxross.cohesive.mellow.icon.regular._explorer!!
  }

private var _explorer: ImageVector? = null
