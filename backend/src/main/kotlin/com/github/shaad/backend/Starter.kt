package com.github.shaad.backend

import com.github.shaad.backend.repository.InMemoryRepositoryModule
import com.github.shaad.backend.service.ServiceModule
import com.github.shaad.backend.web.WebModule
import com.github.shaad.backend.web.WebStarter
import com.google.inject.Guice

fun main() {
    val injector = Guice.createInjector(
        listOf(
            WebModule(),
            ServiceModule(),
            InMemoryRepositoryModule()
        )
    )

    injector.getInstance(WebStarter::class.java).start()
}
