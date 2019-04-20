package com.github.shaad.backend.repository

import com.google.inject.AbstractModule
import com.google.inject.Singleton


interface Repository : AutoCloseable {
    fun commit()
}

interface RepositoryFactory {
    fun getGameRepository() : GameRepository
    fun getUserRepository() : UserRepository
    fun getHighScoreRepository() : HighScoreRepository
}

private class RepositoryFactoryImpl : RepositoryFactory {
    override fun getGameRepository(): GameRepository {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUserRepository(): UserRepository {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getHighScoreRepository(): HighScoreRepository {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class RepositoryModule : AbstractModule() {
    override fun configure() {
        bind(RepositoryFactory::class.java).to(RepositoryFactoryImpl::class.java).`in`(Singleton::class.java)
    }
}