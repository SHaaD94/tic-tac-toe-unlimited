package com.github.shaad.backend.service

import com.google.inject.AbstractModule

class ServiceModule : AbstractModule() {
    override fun configure() {
        install(UserServiceModule())
        install(OpponentSearchServiceModule())
        install(GameServiceModule())
        install(GameValidatorModule())
    }
}