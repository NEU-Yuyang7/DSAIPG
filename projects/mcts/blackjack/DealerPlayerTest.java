package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class DealerPlayerTest {

    private DealerPlayer dealer;
    private Player dummyPlayer; // mock opponent

    @BeforeEach
    void setUp() {
        dummyPlayer = new Player.RandomPlayer();
        dealer = new DealerPlayer(dummyPlayer);
    }

    @Test
    void testActionHitsWhenBelow17() {
        dealer.getCards().add(new Card(7, Suit.Spade));   // total 7
        dealer.getCards().add(new Card(8, Suit.Heart));   // total 15

        Deck deck = new Deck();
        deck.shuffle();
        int before = dealer.getCards().size();

        Action action = dealer.action(deck);

        assertEquals(Action.Hit, action);
        assertEquals(before + 1, dealer.getCards().size());
    }

    @Test
    void testActionStandsWhen17OrMore() {
        dealer.getCards().add(new Card(10, Suit.Spade));  // total 10
        dealer.getCards().add(new Card(7, Suit.Heart));   // total 17

        Deck deck = new Deck();
        Action action = dealer.action(deck);

        assertEquals(Action.Stand, action);
    }

    @Test
    void testCopyCreatesIdenticalCardState() {
        dealer.getCards().add(new Card(10, Suit.Spade));
        dealer.getCards().add(new Card(3, Suit.Heart));

        DealerPlayer copied = dealer.copy(new Player.RandomPlayer());

        assertEquals(2, copied.getCards().size());
        assertEquals(dealer.getCards().get(0).toString(), copied.getCards().get(0).toString());
    }

    @Test
    void testCopyUsesNewOpponent() {
        Player newOpponent = new Player.RandomPlayer();
        DealerPlayer copied = dealer.copy(newOpponent);

        // 用反射获取字段做断言（因为 opponent 是 private）
        try {
            var field = DealerPlayer.class.getDeclaredField("opponent");
            field.setAccessible(true);
            Player copiedOpponent = (Player) field.get(copied);
            assertSame(newOpponent, copiedOpponent);
        } catch (Exception e) {
            fail("Reflection failed: " + e.getMessage());
        }
    }
}
