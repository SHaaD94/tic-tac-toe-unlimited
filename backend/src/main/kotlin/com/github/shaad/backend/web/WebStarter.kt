package com.github.shaad.backend.web

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.google.inject.Inject
import io.ktor.application.install
import io.ktor.features.CallLogging
import io.ktor.features.Compression
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import java.text.DateFormat

class WebStarter @Inject constructor(private val controllers: @JvmSuppressWildcards Set<Controller>) {
    private val server = embeddedServer(Netty, port = 8080) {
        install(Compression)
        install(CallLogging)
        install(ContentNegotiation) {
            jackson {
                enable(SerializationFeature.INDENT_OUTPUT)
                registerModule(KotlinModule())
                dateFormat = DateFormat.getDateInstance()
            }
        }

        routing {
            controllers.forEach { it.setup(this) }
        }
    }

    fun start() {
        server.start()
    }
}