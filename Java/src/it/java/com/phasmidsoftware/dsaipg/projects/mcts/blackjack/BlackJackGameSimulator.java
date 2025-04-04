package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;
/*
1. 一共两种player： 普通玩家、庄家
普通玩家可以：hit。stand
庄家：<17 必须hit()


2. APIs
game:
洗牌：shuffle()
发牌：dealerCard（）
算分：score();
爆: isBusted();
isBackJack():


player apis:
action();//用mcts（）决定hits还是stand

3. Classes

class Deck
class Card
class Player
class DealerPlayers

4.aggregation(has a)    association(relative)   inheritance(is a)

 Deck has (Card)
 Player - Card
 player -- dealerplayer

5. data structure
class Deck
{
Card[] cards;
shuffle();
dealerCard（）
score();
isBusted();
isBackJack();
}
class Card
{
int faceValue;
String suit;
}
class Player
{
action();
List<Card> handCard;
}
class DealerPlayer extends Player
{
@override
action();//超过17才能stand；
}


6. input output

void shuffle()
dealerCard（Card[] cards）
score(handcards,handcards);
isBusted(handcards);
isBackJack(handcards):


action();//
用action mcts（hands,cards,500）决定hits还是stand


7.test:
a.先每人发两张，dealerplayer，player
if(dealerplayer.isBackJack())
{
if(player.isBlackJack)
{
return draw
}
else{
return dealerplayer win;
}
return player win;
}

b.player round:
hit or stand

c.dealer player round
hit or stand

d.all stand then compare

 */
public class BlackJackGameSimulator {
        private Deck deck;
        private Player player;
        private Player dealer;

        public BlackJackGameSimulator() {
            deck = new Deck();
            deck.shuffle();
            player = new Player();
            dealer = new DealerPlayer(player);
            // Initialization: Each player gets 2 cards
            player.hit(deck);
            dealer.hit(deck);
            player.hit(deck);
            dealer.hit(deck);
        }

        private void printStatus() {
            player.printStatus();
            dealer.printStatus();
        }

        public void simulate() {
            if (player.isBlackJack()) {
                if (dealer.isBlackJack()) {
                    System.out.println("-- Draw --");
                } else {
                    System.out.println("-- Player black jack wins --");
                }
                return;
            }

            if (dealer.isBlackJack()) {
                System.out.println("-- Dealer black jack wins --");
                printStatus();
                return;
            }

            System.out.println("-- Player round --");
            while (player.action(this.deck) == Action.Hit) {
                if (player.isBusted()) {
                    System.out.println("-- Dealer wins --");
                    printStatus();
                    return;
                }
            }

            System.out.println("-- Dealer round --");
            while (dealer.action(this.deck) == Action.Hit) {
                if (dealer.isBusted()) {
                    System.out.println("-- Player wins --");
                    printStatus();
                    return;
                }
            }

            if (player.score() > dealer.score()) {
                System.out.println("-- Player wins --");
            } else if (player.score() < dealer.score()) {
                System.out.println("-- Dealer wins --");
            } else {
                System.out.println("-- Draw --");
            }
            printStatus();
        }
    public BlackJackSimulationTest.Result simulateAndReturnResult() {
        if (player.isBlackJack()) {
            if (dealer.isBlackJack()) {
                return BlackJackSimulationTest.Result.DRAW;
            } else {
                return BlackJackSimulationTest.Result.WIN;
            }
        }
        if (dealer.isBlackJack()) {
            return BlackJackSimulationTest.Result.LOSE;
        }

        while (player.action(this.deck) == Action.Hit) {
            if (player.isBusted()) {
                return BlackJackSimulationTest.Result.LOSE;
            }
        }

        while (dealer.action(this.deck) == Action.Hit) {
            if (dealer.isBusted()) {
                return BlackJackSimulationTest.Result.WIN;
            }
        }

        if (player.score() > dealer.score()) {
            return BlackJackSimulationTest.Result.WIN;
        } else if (player.score() < dealer.score()) {
            return BlackJackSimulationTest.Result.LOSE;
        } else {
            return BlackJackSimulationTest.Result.DRAW;
        }
    }
        public static void main(String[] args) {
            BlackJackGameSimulator simulator = new BlackJackGameSimulator();
            simulator.simulate();
        }
    }

