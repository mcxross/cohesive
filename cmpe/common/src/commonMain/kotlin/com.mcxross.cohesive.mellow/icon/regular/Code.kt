package com.mcxross.cohesive.mellow.icon.regular

import androidx.compose.ui.graphics.vector.ImageVector
import com.mcxross.cohesive.mellow.icon.Icons
import com.mcxross.cohesive.mellow.icon.mellowIcon
import com.mcxross.cohesive.mellow.icon.mellowPath

public val Icons.Regular.Code: ImageVector
  get() {
    if (_code != null) {
      return _code!!
    }
    _code = mellowIcon(name = "Regular.Code") {

      mellowPath {
        moveToRelative(5.854f, 4.854f)
        lineToRelative(-0.708f, -0.708f)
        lineToRelative(-3.5f, 3.5f)
        lineToRelative(0.0f, 0.708f)
        lineToRelative(3.5f, 3.5f)
        lineToRelative(0.708f, -0.708f)
        lineToRelative(-3.147f, -3.146f)
        moveToRelative(4.292f, 0.0f)
        lineToRelative(0.708f, -0.708f)
        lineToRelative(3.5f, 3.5f)
        lineToRelative(0.0f, 0.708f)
        lineToRelative(-3.5f, 3.5f)
        lineToRelative(-0.708f, -0.708f)
        lineToRelative(3.147f, -3.146f)
      }

    }
    return _code!!
  }

private var _code: ImageVector? = null
