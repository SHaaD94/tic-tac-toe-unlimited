package com.github.shaad.backend.web

import io.ktor.features.StatusPages
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get

class GameController : Controller {
    override val routingSetup: Routing.() -> Unit = {
        get("$v1Api/games") {
            context.respond("Hello world")
        }
    }

    override val setupExceptionHandling: StatusPages.Configuration.() -> Unit = {}
}
