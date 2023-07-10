package xyz.mcxross.cohesive.designsystem.mellow.icon.regular

import androidx.compose.ui.graphics.vector.ImageVector
import xyz.mcxross.cohesive.designsystem.mellow.icon.Icons
import xyz.mcxross.cohesive.designsystem.mellow.icon.mellowIcon
import xyz.mcxross.cohesive.designsystem.mellow.icon.mellowPath

public val Icons.Regular.Explorer: ImageVector
  get() {
    if (_explorer != null) {
      return _explorer!!
    }
    _explorer = mellowIcon(name = "Regular.Explorer") {

      mellowPath {

      }

    }
    return _explorer!!
  }

private var _explorer: ImageVector? = null
