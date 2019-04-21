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

class GameDTO(
    @JsonProperty("game_id")
    val id: Long?,
    @JsonProperty("first_player_id")
    val firstPlayerId: Long?,
    @JsonProperty("second_player_id")
    val secondPlayerId: Long?,
    @JsonProperty("first_player_symbol")
    val firstPlayerSymbol: Char?,
    @JsonProperty("second_player_symbol")
    val secondPlayerSymbol: Char?,
    @JsonProperty("status")
    val status: String
)

class MoveDTO(
    @JsonProperty("player_id")
    val playerId: Long,
    @JsonProperty("x")
    val x: Int,
    @JsonProperty("y")
    val y: Int
)


class MoveValidationDTO(
    @JsonProperty("status")
    val status: String? = null,
    @JsonProperty("winner_id")
    val secondPlayerId: Long? = null
)

class SearchOpponentCreateDTO(
    @JsonProperty("player_id")
    val playerId: Long
)

class SearchOpponentCancelDTO(
    @JsonProperty("player_id")
    val playerId: Long
)