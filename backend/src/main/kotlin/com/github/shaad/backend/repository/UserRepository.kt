package com.github.shaad.backend.repository

import com.github.shaad.backend.domain.User
import com.github.shaad.backend.domain.UserId
import com.github.shaad.backend.domain.UserNick
import com.github.shaad.backend.domain.UserScore
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

interface UserRepository : Repository {
    fun createUser(nick: String): UserId
    fun getUser(userId: UserId): User?
}

class UserRepositoryImpl : UserRepository, BaseRepository() {
    companion object {
        private val users = ConcurrentHashMap<Long, User>()
        private val idSeq = AtomicLong(0)
    }

    override fun createUser(nick: String): UserId {
        val nextId = idSeq.incrementAndGet()
        users.computeIfAbsent(nextId) { User(UserId(nextId), UserNick(nick), UserScore(0)) }
        return UserId(nextId)
    }

    override fun getUser(userId: UserId): User? {
        return users[userId.id]
    }
}
