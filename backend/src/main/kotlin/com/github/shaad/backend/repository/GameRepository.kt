package com.github.shaad.backend.repository

import com.github.shaad.backend.domain.*
import com.github.shaad.backend.exceptions.CoordinateIsAlreadyTakenException
import com.github.shaad.backend.exceptions.GameNotFoundException
import com.github.shaad.backend.exceptions.NotYourTurnException
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

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

class InMemoryGameRepository : GameRepository, BaseRepository() {
    private data class GameWithMoves(
        val game: Game,
        val moves: LinkedList<Move>,
        val moveCounter: AtomicLong = AtomicLong(0)
    )

    private val rwLock = ReentrantReadWriteLock()
    private val gameId = AtomicLong(0)
    private val id2Game = ConcurrentHashMap<GameId, GameWithMoves>()
    private val userId2Game = ConcurrentHashMap<UserId, LinkedList<GameId>>()

    override fun getNotFinishedGameForUser(userId: UserId): Game? {
        rwLock.read {
            return userId2Game[userId]?.let {
                it.map { id2Game[it]!! }.firstOrNull { it.game.status != GameStatus.Finished }?.game
            }
        }
    }

    override fun getGame(gameId: GameId): Game? {
        rwLock.read {
            return id2Game[gameId]?.game
        }
    }

    override fun createGame(
        firstUserId: UserId,
        secondUserId: UserId,
        firstPlayerSymbol: PlayerSymbol,
        secondPlayerSymbol: PlayerSymbol
    ): GameId {
        rwLock.write {
            val gameId = GameId(gameId.incrementAndGet())

            fun addGameToUser(playerId: UserId) = userId2Game.computeIfAbsent(playerId) { LinkedList() }.add(gameId)

            addGameToUser(firstUserId)
            addGameToUser(secondUserId)

            id2Game[gameId] =
                GameWithMoves(
                    Game(
                        gameId,
                        firstUserId,
                        firstPlayerSymbol,
                        secondUserId,
                        secondPlayerSymbol,
                        GameStatus.InProgress
                    ),
                    LinkedList()
                )
            return gameId
        }
    }

    override fun addMove(gameId: GameId, move: Move) {
        rwLock.write {
            val gameWithMoves = id2Game[gameId] ?: throw GameNotFoundException(gameId)
            val moves = gameWithMoves.moves

            if (moves.isEmpty()) {
                moves.add(move)
                return
            }

            if (moves.any { it.coordinate == move.coordinate }) {
                throw CoordinateIsAlreadyTakenException(move.coordinate)
            }

            val lastMove = moves.maxBy { it.moveId!!.id }!!
            if (lastMove.playerId == move.playerId) {
                throw NotYourTurnException()
            }

            moves.add(move.copy(moveId = MoveId(gameWithMoves.moveCounter.incrementAndGet())))
        }
    }

    override fun getGameMoves(gameId: GameId, fromMove: MoveId?): List<Move> {
        rwLock.read {
            val gameWithMoves = id2Game[gameId] ?: throw GameNotFoundException(gameId)
            if (fromMove == null) return gameWithMoves.moves
            return gameWithMoves.moves.filter { it.moveId!!.id > fromMove.id }
        }
    }

    override fun updateGame(gameId: GameId, status: GameStatus, winner: UserId?) {
        if (status == GameStatus.Finished && winner == null) {
            throw RuntimeException("Finished game must have winner")
        }
        rwLock.write {
            val gameWithMoves = id2Game[gameId] ?: throw GameNotFoundException(gameId)
            id2Game[gameId] = gameWithMoves.copy(game = gameWithMoves.game.copy(status = status, winner = winner))
        }
    }

}
