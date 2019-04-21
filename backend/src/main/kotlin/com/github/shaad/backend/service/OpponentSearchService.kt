package com.github.shaad.backend.service

import com.github.shaad.backend.domain.UserId
import com.github.shaad.backend.exceptions.UserNotFoundException
import com.github.shaad.backend.repository.RepositoryFactory
import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Singleton

interface OpponentSearchService {
    fun enqueueUser(id: UserId)
    fun isInQueue(id: UserId): Boolean
    fun removeFromQueue(id: UserId)
}

class OpponentSearchServiceImpl @Inject constructor(private val repositoryFactory: RepositoryFactory) :
    OpponentSearchService {

    override fun enqueueUser(id: UserId) {
        checkUserExistence(id)
        repositoryFactory.getPlayerQueueRepository().use {
            it.enqueuePlayer(id)
            it.commit()
        }
    }

    override fun isInQueue(id: UserId): Boolean {
        checkUserExistence(id)
        repositoryFactory.getPlayerQueueRepository().use {
            return it.isInQueue(id)
        }
    }

    override fun removeFromQueue(id: UserId) {
        checkUserExistence(id)
        repositoryFactory.getPlayerQueueRepository().use {
            it.removePlayerFromQueue(id)
            it.commit()
        }
    }

    private fun checkUserExistence(id: UserId) {
        repositoryFactory.getUserRepository().use {
            if (it.getUser(id) == null) {
                throw UserNotFoundException(id)
            }
        }
    }
}

class OpponentSearchServiceModule : AbstractModule() {
    override fun configure() {
        bind(OpponentSearchService::class.java).to(OpponentSearchServiceImpl::class.java).`in`(Singleton::class.java)
    }
}