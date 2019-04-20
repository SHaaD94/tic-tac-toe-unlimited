package com.github.shaad.backend

import com.github.shaad.backend.repository.RepositoryModule
import com.github.shaad.backend.service.ServiceModule
import com.github.shaad.backend.web.WebModule
import com.github.shaad.backend.web.WebStarter
import com.google.inject.Guice

fun main() {
    val injector = Guice.createInjector(
        listOf(
            WebModule(),
            ServiceModule(),
            RepositoryModule()
        )
    )

    injector.getInstance(WebStarter::class.java).start()
}
