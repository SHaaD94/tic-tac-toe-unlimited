package com.github.shaad.backend.dto

import com.fasterxml.jackson.annotation.JsonProperty

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