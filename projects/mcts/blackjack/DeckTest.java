package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {

    private Deck deck;

    @BeforeEach
    void setUp() {
        deck = new Deck();
    }

    @Test
    void testInitialDeckHas52Cards() {
        assertEquals(52, deck.remainingCards());
    }

    @Test
    void testDealCardReducesRemainingCards() {
        int before = deck.remainingCards();
        Card c = deck.dealCard();
        assertNotNull(c);
        assertEquals(before - 1, deck.remainingCards());
    }

    @Test
    void testDealAllCardsAndThrowsException() {
        for (int i = 0; i < 52; i++) {
            assertNotNull(deck.dealCard());
        }
        Exception exception = assertThrows(IllegalStateException.class, () -> deck.dealCard());
        assertEquals("No cards remaining", exception.getMessage());
    }

    @Test
    void testCopyDeckMaintainsSameOrder() {
        Deck copy = deck.copy();
        for (int i = 0; i < 5; i++) {
            assertEquals(deck.dealCard().toString(), copy.dealCard().toString());
        }
    }

    @Test
    void testCopyDeckIsIndependent() {
        Deck copy = deck.copy();
        deck.dealCard();
        assertEquals(copy.remainingCards(), deck.remainingCards() + 1);
    }

    @Test
    void testShuffleChangesOrder() {
        Deck original = new Deck();
        Deck shuffled = new Deck();
        shuffled.shuffle();

        // 不保证一定乱，但至少前几张应该不同（概率上）
        boolean isDifferent = false;
        for (int i = 0; i < 5; i++) {
            if (!original.dealCard().toString().equals(shuffled.dealCard().toString())) {
                isDifferent = true;
                break;
            }
        }
        assertTrue(isDifferent, "Shuffled deck should have a different order");
    }
}
