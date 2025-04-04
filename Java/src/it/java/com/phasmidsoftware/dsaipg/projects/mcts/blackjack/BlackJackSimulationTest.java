package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

public class BlackJackSimulationTest {

    public static void main(String[] args) {
        int win = 0;
        int lose = 0;
        int draw = 0;

        int simulations = 100_000;

        for (int i = 0; i < simulations; i++) {
            BlackJackGameSimulator game = new BlackJackGameSimulator();
            Result result = game.simulateAndReturnResult();

            switch (result) {
                case WIN:
                    win++;
                    break;
                case LOSE:
                    lose++;
                    break;
                case DRAW:
                    draw++;
                    break;
            }
        }

        System.out.println("=================================================");
        System.out.println("Simulation " + simulations + " times:");
        System.out.println("Win: " + win);
        System.out.println("Lose: " + lose);
        System.out.println("Draw: " + draw);
        System.out.println("=================================================");
    }

    public enum Result {
        WIN, LOSE, DRAW
    }
}