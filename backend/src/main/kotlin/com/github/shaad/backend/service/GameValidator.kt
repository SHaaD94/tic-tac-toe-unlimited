package com.github.shaad.backend.service

import com.github.shaad.backend.domain.Coordinate
import com.github.shaad.backend.domain.Game
import com.github.shaad.backend.domain.Move
import com.github.shaad.backend.domain.UserId
import com.google.inject.AbstractModule

data class GameValidationResult(val isFinished: Boolean, val winner: UserId?)

interface GameValidator {
    fun validate(game: Game, move: Move): GameValidationResult
}

internal class GameValidatorImpl : GameValidator {
    override fun validate(game: Game, move: Move): GameValidationResult {
        val symbolsInRow = game.winCondition.symbolsInRow
        val symbol = if (game.firstPlayerId == move.playerId) game.firstPlayerSymbol else game.secondPlayerSymbol
        val playerId = if (game.firstPlayerId == move.playerId) game.firstPlayerId else game.secondPlayerId
        val currentGameState = game.gameState.copy(board = game.gameState.board.plus(move.coordinate to symbol))
        val (x, y) = move.coordinate

        fun checkTopToBotDiagonal(): Boolean {
            var curX = x - symbolsInRow
            var curY = y - symbolsInRow
            while (curX <= x && curY <= y) {
                var matched = true
                for (i in 0 until symbolsInRow) {
                    if (currentGameState.board[Coordinate(curX + i, curY + i)] != symbol) {
                        matched = false
                        break
                    }
                }
                if (matched) {
                    return true
                }
                curX++
                curY++
            }
            return false
        }

        fun checkBotToTopDiagonal(): Boolean {
            var curX = x - symbolsInRow
            var curY = y + symbolsInRow
            while (curX <= x && curY >= y) {
                var matched = true
                for (i in 0 until symbolsInRow) {
                    if (currentGameState.board[Coordinate(curX + i, curY - i)] != symbol) {
                        matched = false
                        break
                    }
                }
                if (matched) {
                    return true
                }
                curX++
                curY--
            }
            return false
        }

        fun checkVertical(): Boolean {
            var curY = y - symbolsInRow
            while (curY <= y) {
                var matched = true
                for (i in 0 until symbolsInRow) {
                    if (currentGameState.board[Coordinate(x, curY + i)] != symbol) {
                        matched = false
                        break
                    }
                }
                if (matched) {
                    return true
                }
                curY++
            }
            return false
        }

        fun checkHorizontal(): Boolean {
            var curX = x - symbolsInRow
            while (curX <= y) {
                var matched = true
                for (i in 0 until symbolsInRow) {
                    if (currentGameState.board[Coordinate(curX + i, y)] != symbol) {
                        matched = false
                        break
                    }
                }
                if (matched) {
                    return true
                }
                curX++
            }
            return false
        }

        val isFinished = checkBotToTopDiagonal() || checkTopToBotDiagonal() || checkHorizontal() || checkVertical()

        return GameValidationResult(isFinished, if (isFinished) playerId else null)
    }
}

class GameValidatorModule : AbstractModule() {
    override fun configure() {
        bind(GameValidator::class.java).to(GameValidatorImpl::class.java)
    }
}