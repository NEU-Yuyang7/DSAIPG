package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    private static final Random RANDOM = new Random();
    private final List<Card> cards = new ArrayList<>();
    private int dealtIndex = 0;

    public Deck() {
        for (int v = 1; v <= 13; v++) {
            for (Suit s : Suit.values()) {
                cards.add(new Card(v, s));
            }
        }
    }

    public Deck copy() {
        Deck copy = new Deck();
        copy.cards.clear();
        copy.cards.addAll(this.cards);
        copy.dealtIndex = this.dealtIndex;
        return copy;
    }

    public void shuffle() {
        for (int i = cards.size() - 1; i >= 0; i--) {
            int j = RANDOM.nextInt(i + 1);
            Card tmp = cards.get(i);
            cards.set(i, cards.get(j));
            cards.set(j, tmp);
        }
    }

    public int remainingCards() {
        return cards.size() - dealtIndex;
    }

    public Card dealCard() {
        if (remainingCards() == 0) {
            throw new IllegalStateException("No cards remaining");
        }
        // 在剩余牌中随机选一个索引
        int randomIndex = dealtIndex + RANDOM.nextInt(remainingCards());
        // 与 dealtIndex 交换
        Card selected = cards.get(randomIndex);
        cards.set(randomIndex, cards.get(dealtIndex));
        cards.set(dealtIndex, selected);
        // 返回交换后的牌
        return cards.get(dealtIndex++);
    }

}