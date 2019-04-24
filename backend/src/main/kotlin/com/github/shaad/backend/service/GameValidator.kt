package com.github.shaad.backend.service

import com.github.shaad.backend.domain.Move
import com.github.shaad.backend.domain.UserId
import com.google.inject.AbstractModule

data class GameValidationResult(val isFinished: Boolean, val winner: UserId?)

interface GameValidator {
    fun validate(moves: List<Move>): GameValidationResult
}

private class GameValidatorImpl : GameValidator {
    override fun validate(moves: List<Move>): GameValidationResult {
        //todo
        return GameValidationResult(false, null)
    }
}

class GameValidatorModule : AbstractModule() {
    override fun configure() {
        bind(GameValidator::class.java).to(GameValidatorImpl::class.java)
    }
}