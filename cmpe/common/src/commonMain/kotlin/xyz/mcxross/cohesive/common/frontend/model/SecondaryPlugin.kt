package xyz.mcxross.cohesive.common.frontend.model

import java.io.File

data class SecondaryPlugin(
  val id: String,
  val name: String,
  val icon: String,
  val repo: String,
  val category: String,
  val description: String,
  val author: String
)

fun SecondaryPlugin.isInstalled(): Boolean {
  return File("plugin/secondary/$name").exists()
}
