package xyz.mcxross.cohesive.utils

import io.ktor.client.*

expect fun isInternetAvailable(): Boolean
expect fun getHTTPClient(): HttpClient
