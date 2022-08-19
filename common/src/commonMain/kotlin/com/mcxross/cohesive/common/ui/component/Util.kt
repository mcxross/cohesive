package com.mcxross.cohesive.common.ui.component

internal fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + ((stop - start) * fraction)
}
