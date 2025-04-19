package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

enum  Suit {
    Club,
    Diamond,
    Heart,
    Spade
}

public class Card implements Comparable<Card> {
    private final int faceValue; // 1 for A, 11 J, 12 Q, 13 K
    private final Suit suit;

    public Card(int value, Suit suit) {
        this.faceValue = value;
        this.suit = suit;
    }

    public int value() {
        return faceValue;
    }

    public boolean isAce() {
        return faceValue == 1;
    }

    public boolean isFace() {
        return faceValue >= 10 && faceValue <= 13;
    }

    @Override
    public String toString() {
        return (faceValue == 1 ? "A" :
                faceValue == 11 ? "J" :
                        faceValue == 12 ? "Q" :
                                faceValue == 13 ? "K" : faceValue)
                + " of " + suit;
    }

    @Override
    public int compareTo(Card o) {
        return this.faceValue - o.faceValue;
    }
}
