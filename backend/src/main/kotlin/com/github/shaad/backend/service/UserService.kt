package com.github.shaad.backend.service

import com.github.shaad.backend.domain.UserId
import com.github.shaad.backend.dto.UserDTO
import com.github.shaad.backend.repository.RepositoryFactory
import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Singleton

interface UserService {
    fun createUser(nick: String): UserId
    fun getUser(id: UserId): UserDTO?
}

class UserServiceImpl @Inject constructor(private val repositoryFactory: RepositoryFactory) : UserService {
    override fun createUser(nick: String): UserId {
        repositoryFactory.getUserRepository().use {
            val userId = it.createUser(nick)
            it.commit()
            return userId
        }
    }

    override fun getUser(id: UserId): UserDTO? {
        repositoryFactory.getUserRepository().use {
            val user = it.getUser(id) ?: return null
            return UserDTO(user.id.id, user.nick.nick)
        }
    }
}

class UserServiceModule : AbstractModule() {
    override fun configure() {
        bind(UserService::class.java).to(UserServiceImpl::class.java).`in`(Singleton::class.java)
    }
}