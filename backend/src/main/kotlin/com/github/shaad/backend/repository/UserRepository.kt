package com.github.shaad.backend.repository

import com.github.shaad.backend.domain.User
import com.github.shaad.backend.domain.UserId
import com.github.shaad.backend.domain.UserNick
import com.github.shaad.backend.domain.UserScore
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

interface UserRepository : Repository {
    fun createUser(nick: String): UserId
    fun getUser(userId: UserId): User?
}

class InMemoryUserRepositoryImpl : UserRepository, BaseRepository() {

    private val rwLock = ReentrantReadWriteLock()
    private val users = ConcurrentHashMap<Long, User>()
    private val idSeq = AtomicLong(0)

    override fun createUser(nick: String): UserId {
        rwLock.write {
            val nextId = idSeq.incrementAndGet()
            users.computeIfAbsent(nextId) { User(UserId(nextId), UserNick(nick), UserScore(0)) }
            return UserId(nextId)
        }
    }

    override fun getUser(userId: UserId): User? {
        rwLock.read {
            return users[userId.id]
        }
    }
}
