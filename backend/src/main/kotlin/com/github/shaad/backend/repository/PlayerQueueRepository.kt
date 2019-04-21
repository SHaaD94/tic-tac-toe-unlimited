package com.github.shaad.backend.repository

import com.github.shaad.backend.domain.GameParticipants
import com.github.shaad.backend.domain.UserId
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

interface PlayerQueueRepository : Repository {
    fun enqueuePlayer(userId: UserId)
    fun removePlayerFromQueue(userId: UserId)
    fun getPairOfPlayers(): GameParticipants?
    fun isInQueue(id: UserId): Boolean
}

class InMemoryPlayerQueueRepositoryImpl : PlayerQueueRepository, BaseRepository() {
    private val rwLock = ReentrantReadWriteLock()
    private val innerQueue = ConcurrentLinkedQueue<UserId>()
    private val playersInList = ConcurrentHashMap<UserId, Any>()

    override fun enqueuePlayer(userId: UserId) {
        rwLock.write {
            if (playersInList.containsKey(userId)) return
            innerQueue.add(userId)
            playersInList[userId] = true
        }
    }

    override fun removePlayerFromQueue(userId: UserId) {
        rwLock.write {
            innerQueue.remove(userId)
            playersInList.remove(userId)
        }
    }

    override fun isInQueue(id: UserId): Boolean {
        rwLock.read {
            return playersInList.containsKey(id)
        }
    }

    override fun getPairOfPlayers(): GameParticipants? {
        rwLock.write {
            val first = innerQueue.poll() ?: return null
            val second = innerQueue.poll()
            return if (second == null) {
                innerQueue.add(first)
                null
            } else {
                playersInList.remove(first)
                playersInList.remove(second)
                GameParticipants(first, second)
            }
        }
    }
}
