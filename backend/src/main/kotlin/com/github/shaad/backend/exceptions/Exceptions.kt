package com.github.shaad.backend.exceptions

import com.github.shaad.backend.domain.GameId
import com.github.shaad.backend.domain.UserId
import com.github.shaad.backend.domain.UserNick

class UserAlreadyExistsException(userNick: UserNick) :
    RuntimeException("User with nick ${userNick.nick} already exists")


abstract class NotFoundException(message: String) : RuntimeException(message)
class UserNotFoundExcetion(userId: UserId) : NotFoundException("User with id ${userId.id} is not found")

class GameNotFoundException(gameId: GameId) : NotFoundException("Game with id ${gameId.id} is not found")

class GameIsAlreadyFinishedException(gameId: GameId) :
    NotFoundException("Game with id ${gameId.id} is already finished")

