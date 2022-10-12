package com.mcxross.cohesive.cps

import java.util.Comparator


/**
 * Manager responsible for versions of plugins.
 */
interface VersionManager {
    /**
     * Check if a `constraint` and a `version` match.
     * A possible constrain can be `>=1.0.0 & <2.0.0`.
     *
     * @param version
     * @param constraint
     * @return
     */
    fun checkVersionConstraint(version: String, constraint: String): Boolean

    /**
     * Compare two versions. It's similar with [Comparator.compare].
     *
     * @param v1
     * @param v2
     */
    fun compareVersions(v1: String, v2: String): Int
}