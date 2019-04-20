package com.github.shaad.backend.web

import com.google.inject.AbstractModule
import com.google.inject.multibindings.Multibinder

class WebModule : AbstractModule() {
    override fun configure() {
        val multibinder = Multibinder.newSetBinder(binder(), Controller::class.java)
        multibinder.addBinding().to(GameController::class.java)
        multibinder.addBinding().to(UserController::class.java)
        multibinder.addBinding().to(OpponentSearchController::class.java)
    }
}
