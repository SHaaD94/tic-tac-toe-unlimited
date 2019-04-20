package com.github.shaad.backend.web

import com.google.inject.Inject
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

class WebStarter @Inject constructor(private val controllers: @JvmSuppressWildcards Set<Controller>) {
    private val server = embeddedServer(Netty, port = 8080) {
        routing {
            controllers.forEach { it.setup(this) }
        }
    }

    fun start() {
        server.start()
    }
}