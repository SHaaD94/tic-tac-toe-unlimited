package com.github.shaad.backend.exceptions

import com.github.shaad.backend.domain.Coordinate
import com.github.shaad.backend.domain.GameId
import com.github.shaad.backend.domain.UserId
import com.github.shaad.backend.domain.UserNick

abstract class NotFoundException(message: String) : RuntimeException(message)

class UserNotFoundException(userId: UserId) : NotFoundException("User with id ${userId.id} is not found")
class GameNotFoundException(gameId: GameId) : NotFoundException("Game with id ${gameId.id} is not found")
class NoGameForUserInProgressException(userId: UserId) :
    NotFoundException("No game in progress for user with id ${userId.id} found")


class UserAlreadyExistsException(userNick: UserNick) :
    RuntimeException("User with nick ${userNick.nick} already exists")


class WrongPlayerForGameException(userId: UserId, gameId: GameId) :
    RuntimeException("Player with id ${userId.id} is not participant of game with id ${gameId.id} is not found")


class NotYourTurnException : RuntimeException("Not your turn!")


class CoordinateIsAlreadyTakenException(coordinate: Coordinate) : RuntimeException("$coordinate is already taken")


class GameIsAlreadyFinishedException(gameId: GameId) :
    RuntimeException("Game with id ${gameId.id} is already finished")

