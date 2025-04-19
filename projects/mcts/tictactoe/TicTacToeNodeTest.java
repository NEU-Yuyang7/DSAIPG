package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TicTacToeNodeTest {

    @Test
    void testLeafNodeOnTerminalWin() {
        String input = "X X X\nO O .\n. . .";
        Position position = Position.parsePosition(input, 1); // X just won
        State<TicTacToe> state = new TicTacToe().new TicTacToeState(position);

        TicTacToeNode node = new TicTacToeNode(state);
        assertTrue(node.isLeaf(), "Should be leaf because game is over");
        assertEquals(1, node.playouts(), "Leaf node should have 1 playout");
        assertEquals(2, node.wins(), "Win should be scored as 2");
    }

    @Test
    void testLeafNodeOnDraw() {
        String input = "X O X\nX O X\nO X O";
        Position position = Position.parsePosition(input, 1);
        State<TicTacToe> state = new TicTacToe().new TicTacToeState(position);

        TicTacToeNode node = new TicTacToeNode(state);
        assertTrue(node.isLeaf(), "Should be leaf because board is full");
        assertEquals(1, node.playouts(), "Draw should still count as one playout");
        assertEquals(1, node.wins(), "Draw should count as 1 win point");
    }

    @Test
    void testNonLeafNodeInitiallyEmpty() {
        TicTacToe game = new TicTacToe(123L);
        State<TicTacToe> state = game.start();

        TicTacToeNode node = new TicTacToeNode(state);
        assertFalse(node.isLeaf(), "Initial game state should not be terminal");
        assertEquals(0, node.playouts(), "Should be 0 playouts for non-terminal node");
        assertEquals(0, node.wins(), "Should be 0 wins for unexplored node");
    }

    @Test
    void testAddChildAndBackPropagate() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode root = new TicTacToeNode(game.start());

        // Manually create two child states
        State<TicTacToe> childState1 = root.state().next(root.state().chooseMove(root.state().player()));
        State<TicTacToe> childState2 = root.state().next(root.state().chooseMove(root.state().player()));

        // Add child nodes
        root.addChild(childState1);
        root.addChild(childState2);

        // Manually assign playouts/wins
        for (Node<TicTacToe> child : root.children()) {
            ((TicTacToeNode) child).incrementPlayouts();
            ((TicTacToeNode) child).addWins(2);
        }

        root.backPropagate();
        assertEquals(2, root.children().size());
        assertEquals(2, root.playouts(), "Total playouts should be 2");
        assertEquals(4, root.wins(), "Each child had 2 wins, so total should be 4");
    }

    @Test
    void testParentLinking() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode parent = new TicTacToeNode(game.start());

        State<TicTacToe> childState = parent.state().next(parent.state().chooseMove(parent.state().player()));
        parent.addChild(childState);

        Node<TicTacToe> child = parent.children().iterator().next();
        assertEquals(parent, ((TicTacToeNode) child).getParent(), "Child's parent should point back to parent");
    }

    @Test
    void testWhiteMethod() {
        TicTacToe game = new TicTacToe();
        TicTacToeNode root = new TicTacToeNode(game.start());
        assertTrue(root.white(), "Root node player should be the opener (X)");
    }
}
