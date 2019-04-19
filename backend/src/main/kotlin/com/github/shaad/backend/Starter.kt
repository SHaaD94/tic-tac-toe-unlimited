package com.github.shaad.backend

import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    val server = embeddedServer(Netty, port = 8080) {
        routing {
            get("/api/1.0/games") {
                context.respond("Hello world")
            }
        }
    }
    server.start()
}