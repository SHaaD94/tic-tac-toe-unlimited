package com.github.shaad.backend.web

import com.github.shaad.backend.domain.*
import com.github.shaad.backend.dto.ErrorDTO
import com.github.shaad.backend.dto.MoveDTO
import com.github.shaad.backend.exceptions.CoordinateIsAlreadyTakenException
import com.github.shaad.backend.exceptions.GameIsAlreadyFinishedException
import com.github.shaad.backend.exceptions.NotYourTurnException
import com.github.shaad.backend.exceptions.WrongPlayerForGameException
import com.github.shaad.backend.service.GameService
import com.google.inject.Inject
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.request.receive
import io.ktor.response.respond
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

            context.respond(gameService.getGame(id))
        }

        post("$v1Api/games/{id}/moves") {
            val id = GameId(context.parameters.getOrFail("id").toLong())
            val dto = context.receive(MoveDTO::class)

            context.respond(
                gameService.addMoveAndValidateGame(
                    id,
                    Move(null, UserId(dto.playerId), Coordinate(dto.x, dto.y))
                )
            )
        }

        get("$v1Api/games/{id}/moves") {
            val gameId = GameId(context.parameters.getOrFail("id").toLong())
            val fromMoveId = context.parameters["after"]?.toLong()?.let { MoveId(it) }

            context.respond(gameService.getMovesAfter(gameId, fromMoveId))
        }
    }

    override val setupExceptionHandling: StatusPages.Configuration.() -> Unit = {
        exception<NotYourTurnException> {
            call.respond(ErrorDTO(it.message!!))
        }
        exception<CoordinateIsAlreadyTakenException> {
            call.respond(ErrorDTO(it.message!!))
        }
        exception<WrongPlayerForGameException> {
            call.respond(ErrorDTO(it.message!!))
        }
        exception<GameIsAlreadyFinishedException> {
            call.respond(ErrorDTO(it.message!!))
        }
    }
}
