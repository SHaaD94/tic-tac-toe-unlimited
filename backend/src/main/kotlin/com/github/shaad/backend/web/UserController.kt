package com.github.shaad.backend.web

import com.github.shaad.backend.domain.UserId
import com.github.shaad.backend.dto.UserCreationDTO
import com.github.shaad.backend.dto.UserIdDTO
import com.github.shaad.backend.service.UserService
import com.google.inject.Inject
import io.ktor.application.call
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
    override val setup: Routing.() -> Unit = {
        post("$v1ApiPrefix/users") {
            val userCreationDTO = context.receive(UserCreationDTO::class)
            val createdUserId = userService.createUser(userCreationDTO.nick)
            call.respond(UserIdDTO(createdUserId.id))
        }

        get("$v1ApiPrefix/users/{id}") {
            val id = context.parameters.getOrFail("id").toLong()

            when (val userDTO = userService.getUser(UserId(id))) {
                null -> call.respond(HttpStatusCode.NotFound, "No such user")
                else -> call.respond(userDTO)
            }
        }
    }
}
