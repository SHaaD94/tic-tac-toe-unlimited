package com.github.shaad.backend.repository

import com.github.shaad.backend.domain.UserId

interface UserRepository : Repository {
    fun createUser(nick: String): UserId
}
