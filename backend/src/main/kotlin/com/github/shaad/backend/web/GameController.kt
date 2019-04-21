package com.github.shaad.backend.web

import com.github.shaad.backend.domain.*
import com.github.shaad.backend.dto.MoveDTO
import com.github.shaad.backend.service.GameService
import com.google.inject.Inject
import io.ktor.features.StatusPages
import io.ktor.request.receive
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.getOrFail

class GameController @Inject constructor(private val gameService: GameService) : Controller {
    @KtorExperimentalAPI
    override val routingSetup: Routing.() -> Unit = {
        get("$v1Api/games/{id}") {
            val id = GameId(context.parameters.getOrFail("id").toLong())

            gameService.getGame(id)
        }

        post("$v1Api/games/{id}/moves") {
            val id = GameId(context.parameters.getOrFail("id").toLong())
            val dto = context.receive(MoveDTO::class)

            gameService.addMoveAndValidateGame(id, Move(null, UserId(dto.playerId), Coordinate(dto.x, dto.y)))
        }

        get("$v1Api/games/{id}/moves") {
            val gameId = GameId(context.parameters.getOrFail("id").toLong())
            val fromMoveId = context.parameters["after"]?.toLong()?.let { MoveId(it) }

            gameService.getMovesAfter(gameId, fromMoveId)
        }
    }

    override val setupExceptionHandling: StatusPages.Configuration.() -> Unit = {}
}
