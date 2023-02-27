package com.mcxross.cohesive.mellow.icon.regular

import androidx.compose.ui.graphics.vector.ImageVector
import com.mcxross.cohesive.mellow.icon.Icons
import com.mcxross.cohesive.mellow.icon.mellowIcon
import com.mcxross.cohesive.mellow.icon.mellowPath

public val Icons.Regular.Wallet: ImageVector
  get() {
    if (_wallet != null) {
      return _wallet!!
    }
    _wallet = mellowIcon(name = "Regular.Wallet") {

      mellowPath {
        moveTo(0f, 3f)
        arcToRelative(2f, 2f, 0f, false, true, 2f, -2f)
        horizontalLineToRelative(13.5f)
        arcToRelative(0.5f, 0.5f, 0f, false, true, 0f, 1f)
        horizontalLineToRelative(0.5f)
        verticalLineToRelative(2f)
        arcToRelative(1f, 1f, 0f, false, true, 1f, 1f)
        verticalLineToRelative(8.5f)
        arcToRelative(1.5f, 1.5f, 0f, false, true, -1.5f, 1.5f)
        horizontalLineToRelative(-12f)
        arcToRelative(2.5f, 2.5f, 0f, false, true, -2f, -2.5f)
        verticalLineToRelative(-9.768f)
        arcToRelative(1.5f, 1.5f, 0f, false, false, 1.5f, 1.5f)
        horizontalLineToRelative(12f)
        verticalLineToRelative(-3f)
        arcToRelative(1f, 1f, 0f, false, true, 1f, -1f)
        horizontalLineToRelative(-12f)
        arcToRelative(1f, 1f, 0f, false, false, -1f, 1f)
        close()
      }

    }
    return _wallet!!
  }

private var _wallet: ImageVector? = null
