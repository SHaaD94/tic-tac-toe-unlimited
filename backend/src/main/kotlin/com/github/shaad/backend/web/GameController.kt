package com.github.shaad.backend.web

import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get

class GameController : Controller {
    override val setup: Routing.() -> Unit = {
        get("/$apiPrefix/$v1Prefix/games") {
            context.respond("Hello world")
        }
    }
}
