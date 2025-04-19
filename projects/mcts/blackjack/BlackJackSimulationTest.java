package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

public class BlackJackSimulationTest {

    public static void main(String[] args) {
        int totalGames  = 10_000;
        int[] simCounts = {10, 20, 40, 80};

        for (int sims : simCounts) {
            if (sims == 0) {
                System.out.println("=== Baseline: RandomPlayer ===");
            } else {
                System.out.printf("=== Full MCTSPlayer (%d sims/move) ===%n", sims);
            }
            runExperiment(totalGames, sims);
            System.out.println();
        }
    }

    private static void runExperiment(int totalGames, int mctsSims) {
        int w=0, l=0, d=0;
        long t0 = System.currentTimeMillis();

        for (int i = 0; i < totalGames; i++) {
            Player player = (mctsSims > 0)
                    ? new Player.MCTSPlayer(mctsSims)
                    : new Player.RandomPlayer();
            DealerPlayer dealer = new DealerPlayer(player);

            Deck deck = new Deck();
//            deck.shuffle();
            // 发两张
            player.hit(deck);
            dealer.hit(deck);
            player.hit(deck);
            dealer.hit(deck);

            BlackJackGameSimulator.Result res =
                    new BlackJackGameSimulator(player, dealer, deck)
                            .simulateAndReturnResult(mctsSims);
            switch (res) {
                case WIN  -> w++;
                case LOSE -> l++;
                case DRAW -> d++;
            }
        }

        long elapsed = System.currentTimeMillis() - t0;
        System.out.printf("Games   : %d%n", totalGames);
        System.out.printf("Wins    : %d (%.2f%%)%n", w, 100.0 * w / totalGames);
        System.out.printf("Losses  : %d (%.2f%%)%n", l, 100.0 * l / totalGames);
        System.out.printf("Draws   : %d (%.2f%%)%n", d, 100.0 * d / totalGames);
        System.out.printf("Time    : %d ms%n", elapsed);
    }
}