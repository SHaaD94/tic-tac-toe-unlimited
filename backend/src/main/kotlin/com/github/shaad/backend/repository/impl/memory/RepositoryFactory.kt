package com.github.shaad.backend.repository.impl.memory

import com.github.shaad.backend.repository.*
import com.google.inject.AbstractModule
import com.google.inject.Singleton


private class InMemoryRepositoryFactory : RepositoryFactory {
    private val gameRepository = InMemoryGameRepository()
    private val playerQueueRepository = InMemoryPlayerQueueRepositoryImpl()
    private val userRepository = InMemoryUserRepositoryImpl()

    override fun getGameRepository(): GameRepository {
        return gameRepository
    }

    override fun getPlayerQueueRepository(): PlayerQueueRepository {
        return playerQueueRepository
    }

    override fun getUserRepository(): UserRepository {
        return userRepository
    }

    override fun getHighScoreRepository(): HighScoreRepository {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class InMemoryRepositoryModule : AbstractModule() {
    override fun configure() {
        bind(RepositoryFactory::class.java).to(InMemoryRepositoryFactory::class.java).`in`(Singleton::class.java)
    }
}