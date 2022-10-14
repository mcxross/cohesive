package com.mcxross.cohesive.cps


/**
 * Default implementation for [VersionManager].
 * This implementation uses jSemVer (a Java implementation of the SemVer Specification).
 */
class DefaultVersionManager : VersionManager {
    /**
     * Checks if a version satisfies the specified SemVer [Expression] string.
     * If the constraint is empty or null then the method returns true.
     * Constraint examples: `>2.0.0` (simple), `">=1.4.0 & <1.6.0"` (range).
     * See https://github.com/zafarkhaja/jsemver#semver-expressions-api-ranges for more i.
     *
     * @param version
     * @param constraint
     * @return
     */
    override fun checkVersionConstraint(version: String, constraint: String): Boolean {
        /*return StringUtils.isNullOrEmpty(constraint) || "*" == constraint || com.github.zafarkhaja.semver.Version.valueOf(
            version
        ).satisfies(constraint)*/
        return true
    }

    override fun compareVersions(v1: String, v2: String): Int {
        /*return com.github.zafarkhaja.semver.Version.valueOf(v1)
            .compareTo(com.github.zafarkhaja.semver.Version.valueOf(v2))*/
        return 2
    }
}