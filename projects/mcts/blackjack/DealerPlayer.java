package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

/**
 * 庄家：手牌 < 17 时要牌，否则停牌。
 * 保留对手引用主要给 MCTS rollout 为 dealer 决策用。
 */
public class DealerPlayer extends Player {
    private final Player opponent;

    public DealerPlayer(Player opponent) {
        this.opponent = opponent;
    }

    @Override
    public Action action(Deck deck) {
        // 17 以下必须要牌
        if (score() < 17) {
            hit(deck);
            return Action.Hit;
        } else {
            return Action.Stand;
        }
    }

    /** 供 MCTSAgent rollout 时复制状态 */
    public DealerPlayer copy(Player newOpponent) {
        DealerPlayer d = new DealerPlayer(newOpponent);
        d.cards.addAll(this.cards);
        return d;
    }
}