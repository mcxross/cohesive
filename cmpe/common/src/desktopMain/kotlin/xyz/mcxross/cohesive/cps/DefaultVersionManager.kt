package xyz.mcxross.cohesive.cps

import io.github.z4kn4fein.semver.constraints.toConstraint
import io.github.z4kn4fein.semver.satisfies
import io.github.z4kn4fein.semver.toVersion

/**
 * Default implementation for [VersionManager]. This implementation uses jSemVer (a Java
 * implementation of the SemVer Specification).
 */
class DefaultVersionManager : VersionManager {
  /**
   * Checks if a version satisfies the specified SemVer [Expression] string. If the constraint is
   * empty or null then the method returns true. Constraint examples: `>2.0.0` (simple), `">=1.4.0 &
   * <1.6.0"` (range). See https://github.com/zafarkhaja/jsemver#semver-expressions-api-ranges for
   * more i.
   *
   * @param version
   * @param constraint
   * @return
   */
  override fun checkVersionConstraint(version: String, constraint: String): Boolean {
    return constraint.isEmpty() ||
      "*" == constraint ||
      version.toVersion() satisfies constraint.toConstraint()
  }

  override fun compareVersions(v1: String, v2: String): Int {
    return v1.toVersion(strict = false).compareTo(v2.toVersion(strict = false))
  }
}
