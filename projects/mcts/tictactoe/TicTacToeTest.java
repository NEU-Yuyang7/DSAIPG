package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeTest {

    @Test
    void testInitialStateIsNotTerminal() {
        TicTacToe game = new TicTacToe(42L);
        State<TicTacToe> state = game.start();
        assertFalse(state.isTerminal(), "Initial state should not be terminal");
    }

    @Test
    void testInitialPlayerIsX() {
        TicTacToe game = new TicTacToe();
        State<TicTacToe> state = game.start();
        assertEquals(TicTacToe.X, state.player(), "X should go first");
    }

    @Test
    void testNextStateChangesPlayerAndBoard() {
        TicTacToe game = new TicTacToe();
        State<TicTacToe> state = game.start();
        Move<TicTacToe> move = state.chooseMove(state.player());
        State<TicTacToe> nextState = state.next(move);

        assertNotEquals(state.toString(), nextState.toString(), "State should change after move");
        assertEquals(TicTacToe.O, nextState.player(), "Next player should be O after X");
    }

    @Test
    void testFullBoardIsTerminal() {
        String input = "X O X\nX O X\nO X O";
        Position position = Position.parsePosition(input, 0);
        State<TicTacToe> state = new TicTacToe().new TicTacToeState(position);
        assertTrue(state.isTerminal(), "Full board should be terminal");
    }

    @Test
    void testWinningStateReturnsWinner() {
        String input = "X X X\n. O .\n. . O";
        Position position = Position.parsePosition(input, 1); // last move was by X (1)
        State<TicTacToe> state = new TicTacToe().new TicTacToeState(position);
        Optional<Integer> winner = state.winner();
        assertTrue(winner.isPresent(), "Should detect a winner");
        assertEquals(1, winner.get(), "Winner should be player X (1)");
    }

    @Test
    void testMovesReturnsAllValidMoves() {
        TicTacToe game = new TicTacToe();
        State<TicTacToe> state = game.start();
        Collection<Move<TicTacToe>> moves = state.moves(state.player());
        assertEquals(9, moves.size(), "Should have 9 initial moves");
    }

    @Test
    void testToStringDoesNotThrow() {
        TicTacToe game = new TicTacToe();
        State<TicTacToe> state = game.start();
        assertDoesNotThrow(() -> {
            String s = state.toString();
            assertNotNull(s);
        });
    }
}
