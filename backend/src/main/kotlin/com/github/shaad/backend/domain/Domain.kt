package com.github.shaad.backend.domain

import com.github.shaad.backend.dto.GameDTO

data class UserId(val id: Long)
data class UserScore(val score: Int)
data class UserNick(val nick: String)
data class User(val id: UserId, val nick: UserNick, val score: UserScore)

enum class GameStatus {
    InProgress,
    Finished
}

enum class PlayerSymbol {
    X, O;

    val value = this.name[0]
}

data class WinCondition(val symbolsInRow: Int)
data class GameState(val board: Map<Coordinate, PlayerSymbol> = HashMap())
data class GameId(val id: Long)
data class Game(
    val id: GameId,
    val firstPlayerId: UserId,
    val firstPlayerSymbol: PlayerSymbol,
    val secondPlayerId: UserId,
    val secondPlayerSymbol: PlayerSymbol,
    val currentTurnUserId: UserId,
    val gameState: GameState,
    val status: GameStatus,
    val winCondition: WinCondition,
    val winner: UserId? = null
) {
    fun getPlayerSymbol(userId: UserId) = when {
        firstPlayerId == userId -> firstPlayerSymbol
        secondPlayerId == userId -> secondPlayerSymbol
        else -> throw RuntimeException("Wrong user for game")
    }

    fun toDTO() = GameDTO(
        id.id,
        firstPlayerId.id,
        secondPlayerId.id,
        firstPlayerSymbol.value,
        secondPlayerSymbol.value,
        currentTurnUserId.id,
        status.toString(),
        winner?.id
    )
}

data class GameParticipants(val firstPlayerId: UserId, val secondPlayerId: UserId)

data class MoveId(val id: Long)
data class Coordinate(val x: Int, val y: Int)
data class Move(val moveId: MoveId? = null, val playerId: UserId, val coordinate: Coordinate)

