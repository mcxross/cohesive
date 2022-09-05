package com.mcxross.cohesive.mellow

internal fun lerp(start: Float, stop: Float, fraction: Float): Float {
    return start + ((stop - start) * fraction)
}
