package com.github.shaad.backend.web

import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get

class UserController : Controller {
    override val setup: Routing.() -> Unit = {
        get("/$apiPrefix/$v1Prefix/users") {
            context.respond("Hello world")
        }
    }
}
