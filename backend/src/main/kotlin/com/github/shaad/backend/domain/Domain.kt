package com.github.shaad.backend.domain

data class UserId(val id: Long)
data class UserScore(val score: Int)
data class UserNick(val nick: String)
data class User(val id: GameId, val nick: UserNick, val score: UserScore)

enum class GameStatus {
    InProgress,
    Finished
}

enum class PlayerSymbol {
    X, O
}

data class GameId(val id: Long)
data class Game(
    val id: GameId,
    val firstPlayerId: UserId,
    val firstPlayerSymbol: PlayerSymbol,
    val secondPlayerId: UserId,
    val secondPlayerSymbol: PlayerSymbol,
    val status: GameStatus
)

data class MoveId(val id: Long)
data class Coordinate(val x: Int, val y: Int)
data class Move(val moveId: MoveId, val gameId: GameId, val playerId: UserId, val coordinate: Coordinate)


