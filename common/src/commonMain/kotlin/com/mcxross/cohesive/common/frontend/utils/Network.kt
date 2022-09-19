package com.mcxross.cohesive.common.frontend.utils

import io.ktor.client.*

expect fun isInternetAvailable(): Boolean
expect fun getHTTPClient(): HttpClient