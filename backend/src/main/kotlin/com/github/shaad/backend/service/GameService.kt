package com.github.shaad.backend.service

import com.github.shaad.backend.domain.*
import com.github.shaad.backend.repository.RepositoryFactory
import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Singleton

interface GameService {
    fun createGame(firstUserId: UserId, secondUserId: UserId): Game
    fun getGame(gameId: GameId): Game?
    fun addMove(move: Move)
    fun getMovesAfter(gameId: GameId, moveId: MoveId): Collection<Move>
}

private class GameServiceImpl @Inject constructor(private val repositoryFactory: RepositoryFactory) : GameService {
    override fun createGame(firstUserId: UserId, secondUserId: UserId): Game {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getGame(gameId: GameId): Game? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun addMove(move: Move) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getMovesAfter(gameId: GameId, moveId: MoveId): Collection<Move> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class GameServiceModule : AbstractModule() {
    override fun configure() {
        bind(GameService::class.java).to(GameServiceImpl::class.java).`in`(Singleton::class.java)
    }
}
