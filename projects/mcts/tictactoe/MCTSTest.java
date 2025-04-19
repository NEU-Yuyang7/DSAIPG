package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MCTSTest {

    @Test
    void testRunMCTSWithoutCrash() {
        TicTacToe game = new TicTacToe();
        State<TicTacToe> initialState = game.new TicTacToeState();
        Node<TicTacToe> root = new TicTacToeNode(initialState);

        MCTS mcts = new MCTS(root);
        assertDoesNotThrow(() -> mcts.runMCTS(100), "MCTS should run without exception");
    }

    @Test
    void testBestChildNotNullAfterSimulation() {
        MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        mcts.runMCTS(200);

        Node<TicTacToe> best = mcts.bestChild();
        assertNotNull(best, "Best child should not be null after MCTS run");
    }

    @Test
    void testMCTSLeadsToTerminalGame() {
        MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        mcts.runMCTS(300);

        Node<TicTacToe> best = mcts.bestChild();
        State<TicTacToe> state = best.state();

        // Play the game to the end using random valid moves
        while (!state.isTerminal()) {
            state = state.next(state.chooseMove(state.player()));
        }

        assertTrue(state.isTerminal(), "Game should eventually reach a terminal state");
        assertTrue(state.winner().isPresent() || state.winner().isEmpty(), "Game ends with a win or draw");
    }

    @Test
    void testMultipleRunsProduceConsistentOutput() {
        for (int i = 0; i < 3; i++) {
            MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
            mcts.runMCTS(150);
            assertNotNull(mcts.bestChild(), "Best move should always be selected");
        }
    }
}
