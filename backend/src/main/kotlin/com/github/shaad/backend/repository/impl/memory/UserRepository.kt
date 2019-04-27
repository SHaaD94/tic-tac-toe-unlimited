package com.github.shaad.backend.repository.impl.memory

import com.github.shaad.backend.domain.User
import com.github.shaad.backend.domain.UserId
import com.github.shaad.backend.domain.UserNick
import com.github.shaad.backend.domain.UserScore
import com.github.shaad.backend.repository.BaseRepository
import com.github.shaad.backend.repository.UserRepository
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write


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
