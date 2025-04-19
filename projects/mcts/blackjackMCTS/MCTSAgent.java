package com.phasmidsoftware.dsaipg.projects.mcts.blackjackMCTS;

import com.phasmidsoftware.dsaipg.projects.mcts.blackjack.*;
import java.util.*;
import java.util.stream.*;

/**
 * 标准 MCTS：selection → expansion → simulation → backpropagation。
 */
public class MCTSAgent {

    /**
     * @param p           玩家状态
     * @param d           庄家状态
     * @param deck        牌堆副本
     * @param simulations 本步模拟次数
     */
    public Action searchBestAction(Player p, DealerPlayer d, Deck deck, int simulations) {
        MCTSNode root = new MCTSNode(p, d, deck, null, null);
        Random rng = new Random();
        for (int i = 0; i < simulations; i++) {
            MCTSNode promising = selectPromising(root);
            expand(promising);
            MCTSNode nodeToSim = promising.children.isEmpty()
                    ? promising
                    : promising.children.get(rng.nextInt(promising.children.size()));
            double score = simulate(nodeToSim);
            backpropagate(nodeToSim, score);
        }
        // 选访问次数最多的子节点对应的 action
        return root.children.stream()
                .max(Comparator.comparingInt(c -> c.visits))
                .map(c -> c.action)
                .orElse(Action.Stand);
    }

    private MCTSNode selectPromising(MCTSNode node) {
        while (!node.children.isEmpty()) {
            node = node.children.stream()
                    .max(Comparator.comparingDouble(MCTSNode::getUCTValue))
                    .orElse(node);
        }
        return node;
    }

    private void expand(MCTSNode node) {
        if (node.isFullyExpanded()) return;
        // add Hit child
        if (node.children.stream().noneMatch(c -> c.action == Action.Hit)) {
            Player np = node.player.copy();
            Deck nd = node.deck.copy();
            np.hit(nd);
            DealerPlayer ndp = new DealerPlayer(np.copy());
            node.children.add(new MCTSNode(np, ndp, nd, Action.Hit, node));
        }
        // add Stand child
        if (node.children.stream().noneMatch(c -> c.action == Action.Stand)) {
            Player np = node.player.copy();
            DealerPlayer ndp = new DealerPlayer(np.copy());
            Deck nd = node.deck.copy();
            node.children.add(new MCTSNode(np, ndp, nd, Action.Stand, node));
        }
    }

    private double simulate(MCTSNode node) {
        // rollout 用随机玩家
        Player simP = new Player.RandomPlayer();
        simP.getCards().clear(); simP.getCards().addAll(node.player.getCards());
        DealerPlayer simD = new DealerPlayer(simP);
        simD.getCards().clear(); simD.getCards().addAll(node.dealer.getCards());
        Deck simDeck = node.deck.copy();

        BlackJackGameSimulator sim = new BlackJackGameSimulator(simP, simD, simDeck);
        BlackJackGameSimulator.Result res = sim.simulateAndReturnResult();
        return res == BlackJackGameSimulator.Result.WIN ? 1.0
                : res == BlackJackGameSimulator.Result.DRAW ? 0.5 : 0.0;
    }

    private void backpropagate(MCTSNode node, double score) {
        while (node != null) {
            node.visits++;
            node.wins += score;
            node = node.parent;
        }
    }
}