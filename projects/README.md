Blackjack Simulation Framework

This project simulates Blackjack games using different player strategies:
- RandomPlayer: selects actions randomly
- MCTSPlayer: uses Monte Carlo Tree Search to make strategic decisions

Players play against a DealerPlayer following standard casino rules (must hit if under 17).

Key Components:
- Deck and Card classes simulate a standard 52-card deck.
- Player classes define behaviors and scoring logic.
- Game simulator handles game flow, scoring, and results.

How to Run:

1. Single Game Mode
   - Run `BlackJackSingleGameRunner.java`
   - Output includes each step of the game, from card dealing to final result.

2. Batch Simulation Mode
   - Run `BlackJackSimulationTest.java`
   - Configurable to run thousands of games to compare win rates of different strategies.

3. Unit Testing (Optional)
   - Use JUnit to test key components like Card, Deck, Player, and Dealer logic.
