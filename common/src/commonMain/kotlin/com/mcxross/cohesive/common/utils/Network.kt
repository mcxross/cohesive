package com.mcxross.cohesive.common.utils

import io.ktor.client.*

expect fun isInternetAvailable(): Boolean
expect fun getHTTPClient(): HttpClient