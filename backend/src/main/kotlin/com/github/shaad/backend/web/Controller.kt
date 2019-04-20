package com.github.shaad.backend.web

import io.ktor.features.StatusPages
import io.ktor.routing.Routing

internal const val apiPrefix = "api"
internal const val v1Prefix = "1.0"
internal const val v1Api = "/$apiPrefix/$v1Prefix"

interface Controller {
    val routingSetup: Routing.() -> Unit

    val setupExceptionHandling: StatusPages.Configuration.() -> Unit
}