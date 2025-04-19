package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

public class BlackJackSingleGameRunner {

    public static void main(String[] args) {
        System.out.println("Starting a new game of Blackjack...");

        Deck deck = new Deck();
        deck.shuffle();

        Player player = new Player.RandomPlayer(); // Replace with MCTSPlayer if needed
        DealerPlayer dealer = new DealerPlayer(player);

        // Deal initial cards
        player.hit(deck);
        dealer.hit(deck);
        player.hit(deck);
        dealer.hit(deck);

        System.out.printf("Player's initial hand: %s (Total: %d)%n", player.getCards(), player.score());
        System.out.printf("Dealer's visible card: [%s, ??]%n", dealer.getCards().get(0));

        // Player's turn
        while (true) {
            Action action = player.action(deck);
            System.out.printf("Player chooses to: %s%n", action);

            if (action == Action.Hit) {
                Card newCard = player.getCards().get(player.getCards().size() - 1);
                System.out.printf("Player drew: %s | Hand: %s (Total: %d)%n",
                        newCard, player.getCards(), player.score());

                if (player.isBusted()) {
                    System.out.println("Player busted. Dealer wins.");
                    return;
                }
            } else {
                break;
            }
        }

        // Dealer's turn
        System.out.printf("%nDealer reveals hidden card: %s%n", dealer.getCards().get(1));
        System.out.printf("Dealer's full hand: %s (Total: %d)%n", dealer.getCards(), dealer.score());

        while (true) {
            Action action = dealer.action(deck);
            if (action == Action.Hit) {
                dealer.hit(deck);
                Card newCard = dealer.getCards().get(dealer.getCards().size() - 1);
                System.out.printf("Dealer hits and draws: %s%n", newCard);
                System.out.printf("Dealer's hand: %s (Total: %d)%n", dealer.getCards(), dealer.score());

                if (dealer.isBusted()) {
                    System.out.println("Dealer busted. Player wins.");
                    return;
                }
            } else {
                System.out.println("Dealer stands.");
                break;
            }
        }

        // Compare scores
        int playerScore = player.score();
        int dealerScore = dealer.score();
        System.out.printf("%nFinal Scores - Player: %d | Dealer: %d%n", playerScore, dealerScore);

        if (playerScore > dealerScore) {
            System.out.println("Player wins.");
        } else if (playerScore < dealerScore) {
            System.out.println("Dealer wins.");
        } else {
            System.out.println("It's a draw.");
        }
    }
}