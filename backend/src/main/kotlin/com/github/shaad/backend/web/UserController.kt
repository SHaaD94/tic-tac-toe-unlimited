package com.github.shaad.backend.web

import com.github.shaad.backend.domain.UserId
import com.github.shaad.backend.dto.UserCreationDTO
import com.github.shaad.backend.dto.UserIdDTO
import com.github.shaad.backend.exceptions.UserNotFoundException
import com.github.shaad.backend.service.GameService
import com.github.shaad.backend.service.UserService
import com.google.inject.Inject
import io.ktor.application.call
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.util.KtorExperimentalAPI
import io.ktor.util.getOrFail

class UserController @Inject constructor(
    private val userService: UserService,
    private val gameService: GameService
) : Controller {

    @KtorExperimentalAPI
    override val routingSetup: Routing.() -> Unit = {
        post("$v1Api/users") {
            val userCreationDTO = context.receive(UserCreationDTO::class)
            val createdUserId = userService.createUser(userCreationDTO.nick)
            call.respond(UserIdDTO(createdUserId.id))
        }

        get("$v1Api/users/{id}") {
            val id = UserId(context.parameters.getOrFail("id").toLong())

            val userDTO = userService.getUser(id)

            call.respond(userDTO)
        }

        get("$v1Api/users/{id}/current-game") {
            val id = UserId(context.parameters.getOrFail("id").toLong())

            val gameDTO = gameService.getUserGame(id)
            call.respond(gameDTO)
        }

    }

    override val setupExceptionHandling: StatusPages.Configuration.() -> Unit = {
        exception<UserNotFoundException> { cause ->
            call.respond(HttpStatusCode.NotFound, mapOf("error" to cause.message!!))
        }
    }
}
