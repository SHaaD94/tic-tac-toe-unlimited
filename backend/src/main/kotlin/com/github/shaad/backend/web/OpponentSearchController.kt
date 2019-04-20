package com.github.shaad.backend.web

import com.github.shaad.backend.domain.UserId
import com.github.shaad.backend.dto.SearchOpponentCancelDTO
import com.github.shaad.backend.dto.SearchOpponentCreateDTO
import com.github.shaad.backend.dto.StatusDTO
import com.github.shaad.backend.service.OpponentSearchService
import com.google.inject.Inject
import io.ktor.features.StatusPages
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.delete
import io.ktor.routing.post

class OpponentSearchController @Inject constructor(private val opponentSearch: OpponentSearchService) : Controller {
    override val routingSetup: Routing.() -> Unit = {
        post("$v1Api/opponents-search") {
            val searchOpponentCreateDTO = context.receive<SearchOpponentCreateDTO>()
            opponentSearch.enqueueUser(UserId(searchOpponentCreateDTO.playerId))
            context.respond(StatusDTO("OK"))
        }

        delete("$v1Api/opponents-search") {
            val searchOpponentCancelDTO = context.receive<SearchOpponentCancelDTO>()
            opponentSearch.removeFromQueue(UserId(searchOpponentCancelDTO.playerId))
            context.respond(StatusDTO("OK"))
        }
    }

    override val setupExceptionHandling: StatusPages.Configuration.() -> Unit = {}
}
