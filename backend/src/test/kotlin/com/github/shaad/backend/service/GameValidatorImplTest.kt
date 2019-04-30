package com.github.shaad.backend.service

import com.github.shaad.backend.domain.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

class GameValidatorImplTest {
    private val validator: GameValidator = GameValidatorImpl()

    @Test
    fun `Should properly handle empty moves list`() {
        val validationResult = validator.validate("".parseGame(), Move(null, UserId(1), Coordinate(0, 0)))
        assertAll(
            { assertFalse(validationResult.isFinished) { "Game should not be finished" } },
            { assertEquals(null, validationResult.winner) { "Game should not be ended" } }
        )
    }

    @Test
    fun `Game should not be ended 1`() {
        val game = """
            |   O
            |   O
            | X N X X
            | X O
            | X O
            |   O
            |
        """.parseGame(1)

        val validationResult = validator.validate(game, Move(null, UserId(1), Coordinate(-2, -1)))

        assertAll(
            { assertFalse(validationResult.isFinished) { "Game should not be finished" } },
            { assertEquals(null, validationResult.winner) { "Game should not be ended" } }
        )
    }

    @Test
    fun `Vertical game ending for O`() {
        val game = """
            |
            |   O
            | X O X X
            | X N
            | X O
            |   O
            |
        """.parseGame(2)

        val validationResult = validator.validate(game, Move(null, UserId(2), Coordinate(-2, 0)))

        assertAll(
            { assertTrue(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(UserId(2), validationResult.winner) { "Wrong winner" } }
        )
    }

    @Test
    fun `Vertical game ending for X`() {
        val game = """
            |
            |   X
            | O N O O
            | O X
            | O X
            |   X
            |
        """.parseGame(1)

        val validationResult = validator.validate(game, Move(null, UserId(1), Coordinate(-2, -1)))

        assertAll(
            { assertTrue(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(UserId(1), validationResult.winner) { "Wrong winner" } }
        )
    }

    @Test
    fun `Diagonal game ending for X`() {
        val game = """
            | O O   O O
            | N
            |   X
            |     X
            |       X
            |         X
            | O O O
            | X X X
            | O O
            | X X X X
        """.parseGame(1)

        val validationResult = validator.validate(game, Move(null, UserId(1), Coordinate(-3, -2)))

        assertAll(
            { assertTrue(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(UserId(1), validationResult.winner) { "Wrong winner" } }
        )
    }

    @Test
    fun `Horizontal game ending for X`() {
        val game = """
            |                 O
            |               O
            |             O   X
            |       X X X N X
            |         O
            |       O
        """.parseGame(1)

        val validationResult = validator.validate(game, Move(null, UserId(1), Coordinate(3, 0)))

        assertAll(
            { assertTrue(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(UserId(1), validationResult.winner) { "Wrong winner" } }
        )
    }

    @Test
    fun `Diagonal game ending for O`() {
        val game = """
            |
            |               O
            |             O
            |       X X N X X
            |         O X
            |       O
        """.parseGame(2)

        val validationResult = validator.validate(game, Move(null, UserId(2), Coordinate(2, 0)))

        assertAll(
            { assertTrue(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(UserId(2), validationResult.winner) { "Wrong winner" } }
        )
    }

    @Test
    fun `Game should not be ended 2`() {
        val game = """
            |
            |               O
            |             O
            |       X X N X
            |         O X
            |       O
        """.parseGame(1)

        val validationResult = validator.validate(game, Move(null, UserId(1), Coordinate(2, 0)))

        assertAll(
            { assertFalse(validationResult.isFinished) { "Game should be finished" } },
            { assertEquals(null, validationResult.winner) { "Wrong winner" } }
        )
    }

    private fun String.parseGame(currentUserId: Long = 1) =
        this
            .lines()
            .filter { it.contains("|") }
            .map { it.replaceFirst(" *\\|".toRegex(), "") }
            .mapIndexed { y, line -> y to line.mapIndexed { index, c -> index to c }.filter { it.first % 2 == 1 }.map { it.second } }
            .flatMap { (y, splitLines) ->
                splitLines.mapIndexed { x, symbol -> symbol to Coordinate(x - 3, y - 3) }
            }
            .filter { it.first != ' ' && it.first != 'N' }
            .map { (symb, coord) -> coord to PlayerSymbol.values().first { it.value == symb } }
            .toMap()
            .let {
                Game(
                    GameId(1), UserId(1), PlayerSymbol.X, UserId(2), PlayerSymbol.O,
                    UserId(currentUserId), GameState(it), GameStatus.InProgress, WinCondition(5)
                )
            }
}