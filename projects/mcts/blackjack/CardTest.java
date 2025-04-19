package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {

    @Test
    void testCardValue() {
        Card card = new Card(7, Suit.Heart);
        assertEquals(7, card.value());
    }

    @Test
    void testIsAce() {
        Card ace = new Card(1, Suit.Spade);
        Card notAce = new Card(2, Suit.Club);
        assertTrue(ace.isAce());
        assertFalse(notAce.isAce());
    }

    @Test
    void testIsFace() {
        Card jack = new Card(11, Suit.Spade);
        Card queen = new Card(12, Suit.Heart);
        Card king = new Card(13, Suit.Diamond);
        Card ten = new Card(10, Suit.Club);
        Card nine = new Card(9, Suit.Club);

        assertTrue(jack.isFace());
        assertTrue(queen.isFace());
        assertTrue(king.isFace());
        assertTrue(ten.isFace());
        assertFalse(nine.isFace());
    }

    @Test
    void testToString() {
        assertEquals("A of Spade", new Card(1, Suit.Spade).toString());
        assertEquals("J of Heart", new Card(11, Suit.Heart).toString());
        assertEquals("Q of Club", new Card(12, Suit.Club).toString());
        assertEquals("K of Diamond", new Card(13, Suit.Diamond).toString());
        assertEquals("5 of Spade", new Card(5, Suit.Spade).toString());
    }

    @Test
    void testCompareTo() {
        Card low = new Card(3, Suit.Spade);
        Card high = new Card(10, Suit.Heart);
        assertTrue(low.compareTo(high) < 0);
        assertTrue(high.compareTo(low) > 0);
        assertEquals(0, low.compareTo(new Card(3, Suit.Diamond)));
    }
}
