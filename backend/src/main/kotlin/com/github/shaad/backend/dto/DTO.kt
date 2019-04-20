package com.github.shaad.backend.dto

import com.fasterxml.jackson.annotation.JsonProperty

class StatusDTO(
    @JsonProperty("status")
    val status: String
)
class ErrorDTO(
    @JsonProperty("error")
    val status: String
)

class UserDTO(
    @JsonProperty("id")
    val id: Long,
    @JsonProperty("nick")
    val nick: String
)

class UserCreationDTO(
    @JsonProperty("nick")
    val nick: String
)

class UserIdDTO(
    @JsonProperty("id")
    val id: Long
)

class SearchOpponentCreateDTO(
    @JsonProperty("player_id")
    val playerId: Long
)

class SearchOpponentCancelDTO(
    @JsonProperty("player_id")
    val playerId: Long
)