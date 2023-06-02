package xyz.mcxross.cohesive.cps

import java.util.function.BooleanSupplier

data class PluginRepositoryContainer(
  val repo: PluginRepository,
  val condition: BooleanSupplier = BooleanSupplier { true }
)
