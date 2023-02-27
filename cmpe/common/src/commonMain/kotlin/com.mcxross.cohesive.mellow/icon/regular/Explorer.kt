package com.mcxross.cohesive.mellow.icon.regular

import androidx.compose.ui.graphics.vector.ImageVector
import com.mcxross.cohesive.mellow.icon.Icons
import com.mcxross.cohesive.mellow.icon.mellowIcon
import com.mcxross.cohesive.mellow.icon.mellowPath

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
