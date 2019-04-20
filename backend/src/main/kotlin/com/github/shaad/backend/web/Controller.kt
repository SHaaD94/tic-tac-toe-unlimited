package com.github.shaad.backend.web

import io.ktor.routing.Routing

internal const val apiPrefix = "api"
internal const val v1Prefix = "1.0"
internal const val v1ApiPrefix = "/$apiPrefix/$v1Prefix"

interface Controller {
    val setup: Routing.() -> Unit
}