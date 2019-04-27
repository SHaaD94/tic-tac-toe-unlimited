package com.github.shaad.backend.service

import com.github.shaad.backend.domain.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class GameValidatorImplTest {
    private val validator: GameValidator = GameValidatorImpl()

    @Test
    fun `Game should not be ended 1`() {
        val game = game()

        val moves = """
            |   O
            |   O
            | X X X X
            | X O
            | X O
            |   O
            |
        """.parseMoves(game)

        val validationResult = validator.validate(game, moves)

        assertAll(
            { assertFalse(validationResult.isFinished) { "Game should not be finished" } },
            { assertEquals(null, validationResult.winner) { "Game should not be ended" } }
        )
    }

    @Test
    fun `Vertical game ending for O`() {
        val game = game()

        val moves = """
            |
            |   O
            | X O X X
            | X O
            | X O
            |   O
            |
        """.parseMoves(game)

        val validationResult = validator.validate(game, moves)

        assertAll(
            { assertTrue(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(UserId(2), validationResult.winner) { "Wrong winner" } }
        )
    }

    @Test
    fun `Vertical game ending for X`() {
        val game = game()

        val moves = """
            |
            |   X
            | O X O O
            | O X
            | O X
            |   X
            |
        """.parseMoves(game)

        val validationResult = validator.validate(game, moves)

        assertAll(
            { assertTrue(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(UserId(1), validationResult.winner) { "Wrong winner" } }
        )
    }

    @Test
    fun `Horizontal game ending for O`() {
        val game = game()

        val moves = """
            | O O O O O
            | X
            |   X
            |     X
            |       X
            |         X
            | O O O
            | X X X
            | O O
            | X X X X
        """.parseMoves(game)

        val validationResult = validator.validate(game, moves)

        assertAll(
            { assertTrue(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(UserId(1), validationResult.winner) { "Wrong winner" } }
        )
    }

    @Test
    fun `Horizontal game ending for X`() {
        val game = game()

        val moves = """
            |                 O
            |               O
            |             O   X
            |       X X X X X
            |         O
            |       O
        """.parseMoves(game)

        val validationResult = validator.validate(game, moves)

        assertAll(
            { assertTrue(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(UserId(1), validationResult.winner) { "Wrong winner" } }
        )
    }

    @Test
    fun `Diagonal game ending for O`() {
        val game = game()

        val moves = """
            |
            |               O
            |             O
            |       X X O X X
            |         O
            |       O
        """.parseMoves(game)

        val validationResult = validator.validate(game, moves)

        assertAll(
            { assertTrue(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(UserId(1), validationResult.winner) { "Wrong winner" } }
        )
    }

    @Test
    fun `Diagonal game ending for X`() {
        val game = game()

        val moves = """
            |
            |
            | X O   O
            |   X O
            |     X O
            |       X
            |         x
        """.parseMoves(game)

        val validationResult = validator.validate(game, moves)

        assertAll(
            { assertTrue(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(UserId(1), validationResult.winner) { "Wrong winner" } }
        )
    }

    private fun game(): Game = Game(
        GameId(1), UserId(1), PlayerSymbol.X, UserId(2), PlayerSymbol.O,
        UserId(1), GameStatus.InProgress, WinCondition(5)
    )

    private fun String.parseMoves(game: Game): List<Move> {
        var moveCounter = 0L
        return this
            .trimMargin()
            .lines()
            .mapIndexed { y, line -> y to line.split(" ") }
            .filter { it.second.isNotEmpty() }
            .flatMap { (y, splitLines) ->
                splitLines.mapIndexed { x, symbol ->
                    symbol to Coordinate(x - 3, y - 3)
                }
            }
            .mapNotNull { (symbString, coord) ->
                if (symbString.isEmpty()) null else symbString[0] to coord
            }
            .filter { it.first != ' ' }
            .map { (symb, coord) ->
                Move(
                    MoveId(++moveCounter),
                    if (symb == game.firstPlayerSymbol.value) game.firstPlayerId else game.secondPlayerId,
                    coord
                )
            }
    }
}