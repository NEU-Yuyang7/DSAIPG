package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

import com.phasmidsoftware.dsaipg.projects.mcts.blackjackMCTS.MCTSAgent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * 玩家基类，既提供随机策略，也提供每步调用 MCTS 的接口。
 */
public class Player {

    private static final Random RNG = new Random();
    private static final double HIT_RATIO = 0.5;

    protected final List<Card> cards = new ArrayList<>();

    /** Baseline: 随机要牌或停牌 */
    public Action action(Deck deck) {
        if (RNG.nextDouble() < HIT_RATIO||score()<12) {
            hit(deck);
            return Action.Hit;
        } else {
            return Action.Stand;
        }
    }

    /**
     * MCTS 接口：在每一步（包括后续回合）都调用此方法决策。
     * @param deck      当前牌堆
     * @param dealer    庄家对象（供 MCTS rollout 参考）
     * @param simulations  本步模拟次数
     */
    public Action mctsAction(Deck deck, DealerPlayer dealer, int simulations) {
        return new MCTSAgent()
                .searchBestAction(this.copy(), dealer.copy(this), deck.copy(), simulations);
    }

    /** 复制自身（浅拷贝手牌列表） */
    public Player copy() {
        Player p = new Player();
        p.cards.addAll(this.cards);
        return p;
    }

    public void hit(Deck deck) {
        cards.add(deck.dealCard());
    }

    public int score() {
        Collections.sort(cards, Collections.reverseOrder());
        int sum = 0, i = 0;
        for (; i < cards.size(); i++) {
            Card c = cards.get(i);
            if (c.isFace()) sum += 10;
            else if (!c.isAce()) sum += c.value();
            else break;
        }
        int aces = cards.size() - i;
        if (aces == 0) return sum <= 21 ? sum : 0;
        int max = aces + 10, min = aces;
        if (sum + min > 21) return 0;
        if (sum + max > 21) return sum + min;
        return sum + max;
    }

    public boolean isBusted() {
        return score() == 0;
    }

    public boolean isBlackjack() {
        if (cards.size() != 2) return false;
        Card a = cards.get(0), b = cards.get(1);
        return (a.isAce() && b.isFace()) || (b.isAce() && a.isFace());
    }

    public List<Card> getCards() {
        return cards;
    }

    /** 随机玩家，直接用基类的 action(Deck) */
    public static class RandomPlayer extends Player { }

    /** 全程 MCTS 玩家：每一步都走 mctsAction(...) */
    public static class MCTSPlayer extends Player {
        private final int simulations;
        public MCTSPlayer(int simulations) {
            this.simulations = simulations;
        }
        @Override
        public Action action(Deck deck) {
            throw new UnsupportedOperationException("请用 mctsAction(...) 来决策");
        }
        /** 在模拟器里调用 */
        public Action decide(Deck deck, DealerPlayer dealer) {
            return mctsAction(deck, dealer, simulations);
        }
        @Override
        public MCTSPlayer copy() {
            MCTSPlayer p = new MCTSPlayer(simulations);
            p.cards.addAll(this.cards);
            return p;
        }
    }
}