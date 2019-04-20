package com.github.shaad.backend.web

import com.github.shaad.backend.domain.UserId
import com.github.shaad.backend.dto.UserCreationDTO
import com.github.shaad.backend.dto.UserIdDTO
import com.github.shaad.backend.exceptions.UserNotFoundExcetion
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

class UserController @Inject constructor(private val userService: UserService) : Controller {
    @KtorExperimentalAPI
    override val routingSetup: Routing.() -> Unit = {
        post("$v1Api/users") {
            val userCreationDTO = context.receive(UserCreationDTO::class)
            val createdUserId = userService.createUser(userCreationDTO.nick)
            call.respond(UserIdDTO(createdUserId.id))
        }

        get("$v1Api/users/{id}") {
            val id = context.parameters.getOrFail("id").toLong()

            when (val userDTO = userService.getUser(UserId(id))) {
                null -> throw UserNotFoundExcetion(UserId(id))
                else -> call.respond(userDTO)
            }
        }
    }

    override val setupExceptionHandling: StatusPages.Configuration.() -> Unit = {
        exception<UserNotFoundExcetion> { cause ->
            call.respond(HttpStatusCode.NotFound, mapOf("error" to cause.message!!))
        }
    }
}
