package com.mcxross.cohesive.cps

/**
 * A holder descriptor contains information about a plug-in.
 */
interface PluginDescriptor {
  var pluginId: String
  val pluginDescription: String?
  val pluginClass: String?
  val version: String
  val requires: String?
  val provider: String?
  val license: String
  val dependencies: List<PluginDependency>?
}
