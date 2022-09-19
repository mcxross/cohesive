package com.mcxross.cohesive.common.frontend.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Configuration(val chain: Chain)

@Serializable
data class Chain(
    @SerialName("clusterKey") val clusterKey: List<String?>,
    @SerialName("clusterValue") val clusterValue: List<String>,
)
