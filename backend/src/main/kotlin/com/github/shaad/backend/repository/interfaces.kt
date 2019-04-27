package com.github.shaad.backend.repository

import com.github.shaad.backend.domain.*


interface Repository : AutoCloseable {
    fun commit()
}

abstract class BaseRepository : Repository {
    override fun commit() = Unit
    override fun close() = Unit
}

interface RepositoryFactory {
    fun getGameRepository(): GameRepository
    fun getPlayerQueueRepository(): PlayerQueueRepository
    fun getUserRepository(): UserRepository
    fun getHighScoreRepository(): HighScoreRepository
}

interface UserRepository : Repository {
    fun createUser(nick: String): UserId
    fun getUser(userId: UserId): User?
}

interface GameRepository : Repository {
    fun getNotFinishedGameForUser(userId: UserId): Game?

    fun getGame(gameId: GameId): Game?

    fun createGame(
        firstUserId: UserId,
        secondUserId: UserId,
        firstPlayerSymbol: PlayerSymbol,
        secondPlayerSymbol: PlayerSymbol
    ): GameId

    fun addMove(gameId: GameId, move: Move)

    fun getGameMoves(gameId: GameId, fromMove: MoveId? = null): List<Move>

    fun updateGame(gameId: GameId, status: GameStatus, winner: UserId?)
}

interface HighScoreRepository : Repository {

}

interface PlayerQueueRepository : Repository {
    fun enqueuePlayer(userId: UserId)
    fun removePlayerFromQueue(userId: UserId)
    fun getPairOfPlayers(): GameParticipants?
    fun isInQueue(id: UserId): Boolean
}
