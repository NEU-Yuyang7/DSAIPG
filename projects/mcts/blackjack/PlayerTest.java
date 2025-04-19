package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player.RandomPlayer();
    }

    @Test
    void testHitAddsCard() {
        Deck deck = new Deck();
        deck.shuffle();
        int before = player.getCards().size();
        player.hit(deck);
        int after = player.getCards().size();
        assertEquals(before + 1, after);
    }

    @Test
    void testScoreWithNormalCards() {
        player.getCards().add(new Card(10, Suit.Spade));  // 10
        player.getCards().add(new Card(7, Suit.Heart));   // 7
        assertEquals(17, player.score());
    }

    @Test
    void testScoreWithAceAs11() {
        player.getCards().add(new Card(1, Suit.Spade));   // A
        player.getCards().add(new Card(9, Suit.Heart));   // 9
        assertEquals(20, player.score());
    }

    @Test
    void testScoreWithAceAs1() {
        player.getCards().add(new Card(1, Suit.Spade));   // A
        player.getCards().add(new Card(10, Suit.Heart));  // 10
        player.getCards().add(new Card(5, Suit.Club));    // 5 -> A只能作为1了
        assertEquals(16, player.score());
    }

    @Test
    void testIsBlackjackTrue() {
        player.getCards().add(new Card(1, Suit.Spade));   // A
        player.getCards().add(new Card(13, Suit.Diamond)); // K
        assertTrue(player.isBlackjack());
    }

    @Test
    void testIsBlackjackFalseWith3Cards() {
        player.getCards().add(new Card(1, Suit.Spade));
        player.getCards().add(new Card(10, Suit.Diamond));
        player.getCards().add(new Card(2, Suit.Heart));
        assertFalse(player.isBlackjack());
    }

    @Test
    void testIsBustedTrue() {
        player.getCards().add(new Card(10, Suit.Spade));
        player.getCards().add(new Card(10, Suit.Heart));
        player.getCards().add(new Card(5, Suit.Club));
        assertTrue(player.isBusted());
    }

    @Test
    void testIsBustedFalse() {
        player.getCards().add(new Card(10, Suit.Spade));
        player.getCards().add(new Card(9, Suit.Heart));
        assertFalse(player.isBusted());
    }

    @Test
    void testCopyMaintainsCardList() {
        player.getCards().add(new Card(10, Suit.Spade));
        player.getCards().add(new Card(7, Suit.Heart));
        Player copied = player.copy();
        List<Card> original = player.getCards();
        List<Card> copy = copied.getCards();

        assertEquals(original.size(), copy.size());
        assertEquals(original.get(0).toString(), copy.get(0).toString());
    }
}
