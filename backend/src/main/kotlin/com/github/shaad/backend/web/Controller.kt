package com.github.shaad.backend.web

import io.ktor.routing.Routing

const val apiPrefix = "api"
const val v1Prefix = "1.0"

interface Controller {
    val setup: Routing.() -> Unit
}