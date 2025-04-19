package com.phasmidsoftware.dsaipg.projects.mcts.blackjackMCTS;

import com.phasmidsoftware.dsaipg.projects.mcts.blackjack.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MCTSAgentTest {

    @Test
    void testSearchBestActionReturnsValidAction() {
        Player.MCTSPlayer player = new Player.MCTSPlayer(10);
        DealerPlayer dealer = new DealerPlayer(player);
        Deck deck = new Deck();
        deck.shuffle();

        // 发牌两张给玩家、庄家
        player.hit(deck);
        dealer.hit(deck);
        player.hit(deck);
        dealer.hit(deck);

        MCTSAgent agent = new MCTSAgent();
        Action result = agent.searchBestAction(player, dealer, deck, 10); // 模拟 10 次

        assertNotNull(result);
        assertTrue(result == Action.Hit || result == Action.Stand);
    }

    @Test
    void testSearchWithMultipleSimulations() {
        Player.MCTSPlayer player = new Player.MCTSPlayer(50);
        DealerPlayer dealer = new DealerPlayer(player);
        Deck deck = new Deck();
        deck.shuffle();

        player.hit(deck);
        dealer.hit(deck);
        player.hit(deck);
        dealer.hit(deck);

        MCTSAgent agent = new MCTSAgent();
        Action result = agent.searchBestAction(player, dealer, deck, 50); // 模拟 50 次

        System.out.println("Best action after 50 simulations: " + result);
        assertNotNull(result);
    }

    @Test
    void testSearchWithHighSimulationsDoesNotCrash() {
        Player.MCTSPlayer player = new Player.MCTSPlayer(200);
        DealerPlayer dealer = new DealerPlayer(player);
        Deck deck = new Deck();
        deck.shuffle();

        player.hit(deck);
        dealer.hit(deck);
        player.hit(deck);
        dealer.hit(deck);

        MCTSAgent agent = new MCTSAgent();
        Action result = agent.searchBestAction(player, dealer, deck, 200); // 模拟 200 次

        assertNotNull(result);
    }
}
