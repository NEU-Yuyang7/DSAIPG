package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

public class MCTS {

    public static void main(String[] args) {
        MCTS mcts = new MCTS(new TicTacToeNode(new TicTacToe().new TicTacToeState()));
        mcts.runMCTS(500); // Run MCTS for 500 iterations

        Node<TicTacToe> best = mcts.bestChild();
        State<TicTacToe> state = best.state();

        // Continue the game from the best move until terminal state
        while (!state.isTerminal()) {
            state = state.next(state.chooseMove(state.player()));
        }

        // Output the winner only
        if (state.winner().isPresent()) {
            System.out.println("Winner: Player " + state.winner().get());
        } else {
            System.out.println("Result: Draw");
        }
    }

    public MCTS(Node<TicTacToe> root) {
        this.root = root;
    }

    private final Node<TicTacToe> root;

    public void runMCTS(int iterations) {
        for (int i = 0; i < iterations; i++) {
            TicTacToeNode promisingNode = selectPromisingNode((TicTacToeNode) root);
            if (!promisingNode.isLeaf()) {
                promisingNode.explore(); // Expand children
            }
            TicTacToeNode leaf = selectPromisingNode(promisingNode); // Select leaf node for simulation
            int result = simulatePlayout(leaf);
            backPropagate(leaf, result);
        }
    }

    private TicTacToeNode selectPromisingNode(TicTacToeNode node) {
        while (!node.isLeaf() && !node.children().isEmpty()) {
            node = (TicTacToeNode) findBestUCTNode(node);
        }
        return node;
    }

    private Node<TicTacToe> findBestUCTNode(Node<TicTacToe> node) {
        double c = Math.sqrt(2); // Exploration constant
        int parentPlayouts = node.playouts();
        Node<TicTacToe> best = null;
        double maxUCT = Double.NEGATIVE_INFINITY;

        for (Node<TicTacToe> child : node.children()) {
            if (child.playouts() == 0) return child;
            double winRate = (double) child.wins() / child.playouts();
            double uct = winRate + c * Math.sqrt(Math.log(parentPlayouts) / child.playouts());
            if (uct > maxUCT) {
                maxUCT = uct;
                best = child;
            }
        }
        return best;
    }

    private int simulatePlayout(TicTacToeNode node) {
        State<TicTacToe> current = node.state();
        int player = current.player();

        while (!current.isTerminal()) {
            current = current.next(current.chooseMove(current.player()));
        }

        if (current.winner().isEmpty()) return 1; // Draw
        return current.winner().get() == player ? 2 : 0; // Win = 2, Loss = 0
    }

    private void backPropagate(TicTacToeNode node, int result) {
        while (node != null) {
            node.incrementPlayouts();
            node.addWins(result);
            node = node.getParent();
        }
    }

    public Node<TicTacToe> bestChild() {
        Node<TicTacToe> best = null;
        double bestRate = -1.0;

        for (Node<TicTacToe> child : root.children()) {
            if (child.playouts() == 0) continue;
            double winRate = (double) child.wins() / child.playouts();
            if (winRate > bestRate) {
                bestRate = winRate;
                best = child;
            }
        }
        return best;
    }
}