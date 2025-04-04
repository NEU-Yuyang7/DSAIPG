package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

enum  Suit {
    Club,
    Diamond,
    Heart,
    Spade
}
public class Card implements Comparable<Card> {
    private final int faceValue; // 1 for A, 11 for J, 12 for Q, 13 for K. Or we can use enum here.
    // private final FaceValue v;
    private final Suit suit;

    public Card(int c, Suit s) {
        faceValue = c;
        suit = s;
    }

    public int value() {
        return faceValue;
    }

    public Suit suit() {
        return suit;
    }

    public boolean isAce() {
        return faceValue == 1;
    }

    public boolean isFace() {
        return faceValue >= 10 && faceValue <= 13;
    }

    @Override
    public String toString() {
        return "Card { " +
                "faceValue = " + faceValue +
                ", suit = " + suit +
                " }";
    }

    @Override
    public int compareTo(Card o) {
        return this.faceValue - o.faceValue;
    }
}
