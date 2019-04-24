package com.github.shaad.backend.service

import com.github.shaad.backend.domain.*
import com.github.shaad.backend.dto.GameDTO
import com.github.shaad.backend.dto.MoveDTO
import com.github.shaad.backend.dto.MoveValidationDTO
import com.github.shaad.backend.exceptions.GameNotFoundException
import com.github.shaad.backend.exceptions.NoGameForUserInProgressException
import com.github.shaad.backend.exceptions.UserNotFoundException
import com.github.shaad.backend.repository.RepositoryFactory
import com.github.shaad.backend.util.WithLogger
import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Singleton
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random

interface GameService {
    fun createGame(firstUserId: UserId, secondUserId: UserId): GameId
    fun getUserGame(userId: UserId): GameDTO
    fun getGame(gameId: GameId): GameDTO
    fun getMovesAfter(gameId: GameId, moveId: MoveId?): Collection<MoveDTO>
    fun addMoveAndValidateGame(gameId: GameId, move: Move): MoveValidationDTO
}

private class GameServiceImpl @Inject constructor(
    private val repositoryFactory: RepositoryFactory,
    private val gameValidator: GameValidator
) : GameService, WithLogger {

    init {
        fixedRateTimer("Game creator", daemon = true, initialDelay = 1000, period = 1000) {
            repositoryFactory.getPlayerQueueRepository().use { playerQueueRepo ->
                while (true) {
                    val gameParticipants = playerQueueRepo.getPairOfPlayers() ?: break
                    createGame(gameParticipants.firstPlayerId, gameParticipants.secondPlayerId)
                    playerQueueRepo.commit()
                    log.info("Creating game for users ${gameParticipants.firstPlayerId.id} and ${gameParticipants.secondPlayerId.id}")
                }
            }
        }
    }

    override fun getUserGame(userId: UserId): GameDTO {
        checkUserExistence(userId)

        val enqueued = repositoryFactory.getPlayerQueueRepository().use {
            it.isInQueue(userId)
        }

        //todo refactor this later
        if (enqueued) {
            return GameDTO(
                null,
                null,
                null,
                null,
                null,
                null,
                "SearchInProgress"
            )
        }
        return repositoryFactory.getGameRepository().use {
            it.getNotFinishedGameForUser(userId)?.toDTO() ?: throw NoGameForUserInProgressException(userId)
        }
    }

    override fun createGame(firstUserId: UserId, secondUserId: UserId): GameId {
        val randomInt = Random.nextInt()
        val firstSymbol = if (randomInt % 2 == 0) PlayerSymbol.O else PlayerSymbol.X
        val secondSymbol = if (randomInt % 2 == 1) PlayerSymbol.O else PlayerSymbol.X

        return repositoryFactory.getGameRepository().use {
            it.createGame(firstUserId, secondUserId, firstSymbol, secondSymbol)
        }
    }

    override fun getGame(gameId: GameId): GameDTO {
        repositoryFactory.getGameRepository().use {
            return it.getGame(gameId)?.toDTO() ?: throw GameNotFoundException(gameId)
        }
    }

    override fun addMoveAndValidateGame(gameId: GameId, move: Move): MoveValidationDTO {
        repositoryFactory.getGameRepository().use {
            it.addMove(gameId, move)

            val validationResult = gameValidator.validate(it.getGameMoves(gameId))
            if (validationResult.isFinished) {
                it.updateGame(gameId, GameStatus.Finished, validationResult.winner!!)
            }

            it.commit()

            return MoveValidationDTO("OK", validationResult.winner?.id)
        }
    }

    override fun getMovesAfter(gameId: GameId, moveId: MoveId?): Collection<MoveDTO> {
        repositoryFactory.getGameRepository().use {
            return it.getGameMoves(gameId, moveId)
                .map { MoveDTO(it.playerId.id, it.coordinate.x, it.coordinate.y, it.moveId!!.id) }
        }
    }

    private fun checkUserExistence(userId: UserId) {
        repositoryFactory.getUserRepository().use {
            it.getUser(userId) ?: throw UserNotFoundException(userId)
        }
    }
}

class GameServiceModule : AbstractModule() {
    override fun configure() {
        bind(GameService::class.java).to(GameServiceImpl::class.java).`in`(Singleton::class.java)
    }
}
