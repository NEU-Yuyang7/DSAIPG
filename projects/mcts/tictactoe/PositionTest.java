package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class PositionTest {

    @Test
    void testParsePosition() {
        String input = "X . O\nO X .\n. . .";
        Position p = Position.parsePosition(input, 1); // last move by player 1 (X)
        assertEquals(5, p.moves(0).size()); // Next: player 0 (O)
    }

    @Test
    void testMoveUpdatesBoard() {
        Position p = Position.parsePosition(". . .\n. . .\n. . .", -1);
        Position p2 = p.move(0, 1, 1);
        assertNotEquals(p, p2);
        assertThrows(RuntimeException.class, () -> p2.move(0, 0, 0)); // Same player again not allowed
    }

    @Test
    void testMovesListsCorrectEmptyCells() {
        String input = "X . O\nO X .\n. . .";
        Position p = Position.parsePosition(input, 1);
        List<int[]> moves = p.moves(0); // Next move is by player 0
        assertEquals(5, moves.size());
    }

    @Test
    void testRotateRotatesCorrectly() {
        String input = "X . .\n. O .\n. . X";
        Position p = Position.parsePosition(input, 1);
        Position rotated = p.rotate();
        String expected = ". . X\n. O .\nX . .";
        assertEquals(expected, rotated.render());
    }

    @Test
    void testReflectRow() {
        String input = "X . .\n. O .\n. . X";
        Position p = Position.parsePosition(input, 1);
        Position reflected = p.reflect(0); // Horizontal reflection
        String expected = ". . X\n. O .\nX . .";
        assertEquals(expected, reflected.render());
    }

    @Test
    void testReflectColumn() {
        String input = "X . .\n. O .\n. . X";
        Position p = Position.parsePosition(input, 1);
        Position reflected = p.reflect(1); // Vertical reflection
        String expected = ". . X\n. O .\nX . .";
        assertEquals(expected, reflected.render());
    }

    @Test
    void testThreeInARowHorizontally() {
        String input = "X X X\n. . .\n. . .";
        Position p = Position.parsePosition(input, 1);
        assertTrue(p.threeInARow());
    }

    @Test
    void testThreeInARowDiagonally() {
        String input = "X . .\n. X .\n. . X";
        Position p = Position.parsePosition(input, 1);
        assertTrue(p.threeInARow());
    }

    @Test
    void testWinnerDetection() {
        String input = "O O O\n. X X\n. . .";
        Position p = Position.parsePosition(input, 0); // last move by player 0 (O)
        Optional<Integer> winner = p.winner();
        assertTrue(winner.isPresent(), "Should detect a winner");
        assertEquals(0, winner.get());
    }

    @Test
    void testFullBoardReturnsTrue() {
        String input = "X O X\nX O X\nO X O";
        Position p = Position.parsePosition(input, 1);
        assertTrue(p.full());
    }

    @Test
    void testEqualsAndHashCode() {
        String input = "X O X\n. . .\n. . .";
        Position p1 = Position.parsePosition(input, 1);
        Position p2 = Position.parsePosition(input, 1);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void testRenderOutput() {
        String input = "X O .\n. X .\n. . O";
        Position p = Position.parsePosition(input, 1);
        String rendered = p.render();
        String expected = "X O .\n. X .\n. . O";
        assertEquals(expected, rendered);
    }
}
