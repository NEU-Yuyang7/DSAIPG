package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

public class DealerPlayer extends Player {
    private static final int DELAER_MUST_HIT = 17;
    private Player player;

    public DealerPlayer(Player player) {
        this.player = player;
    }

    @Override
    public Action action(Deck d) {
        if (score() < DELAER_MUST_HIT) {
            System.out.println("Dealer must hit under 17");
            printStatus();
            hit(d);
            return Action.Hit;
        } else {
            if (score() > this.player.score()) {
                stand();
                return Action.Stand;
            } else {
                return super.action(d);
            }
        }
    }
}