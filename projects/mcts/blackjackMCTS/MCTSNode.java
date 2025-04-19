package com.phasmidsoftware.dsaipg.projects.mcts.blackjackMCTS;

import com.phasmidsoftware.dsaipg.projects.mcts.blackjack.*;

import java.util.ArrayList;
import java.util.List;

public class MCTSNode {
    public final Player player;
    public final DealerPlayer dealer;
    public final Deck deck;
    public final Action action;
    public final MCTSNode parent;
    public final List<MCTSNode> children = new ArrayList<>();
    public int visits = 0;
    public double wins = 0.0;

    public MCTSNode(Player player, DealerPlayer dealer, Deck deck,
                    Action action, MCTSNode parent) {
        this.player = player;
        this.dealer = dealer;
        this.deck = deck;
        this.action = action;
        this.parent = parent;
    }

    public boolean isFullyExpanded() {
        return children.size() == 2;
    }

    public double getUCTValue() {
        if (visits == 0) return Double.MAX_VALUE;
        double uct = (wins / visits)
                + Math.sqrt(2 * Math.log(parent.visits) / visits);
        return uct;
    }
}