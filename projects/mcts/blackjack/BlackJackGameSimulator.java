package com.phasmidsoftware.dsaipg.projects.mcts.blackjack;

public class BlackJackGameSimulator {

    public enum Result { WIN, LOSE, DRAW }

    private final Player player;
    private final DealerPlayer dealer;
    private final Deck deck;

    public BlackJackGameSimulator(Player p, DealerPlayer d, Deck deck) {
        this.player = p;
        this.dealer = d;
        this.deck = deck;
    }

    /** 默认：全程随机 */
    public Result simulateAndReturnResult() {
        return simulateAndReturnResult(0);
    }

    /**
     * @param mctsSims 如果 >0，则玩家每一步都用 MCTS 决策，否则用随机。
     */
    public Result simulateAndReturnResult(int mctsSims) {
        // 先检测 Blackjack
        if (player.isBlackjack() && !dealer.isBlackjack()) return Result.WIN;
        if (!player.isBlackjack() && dealer.isBlackjack()) return Result.LOSE;
        if (player.isBlackjack() && dealer.isBlackjack()) return Result.DRAW;

        // 玩家回合
        if (mctsSims > 0 && player instanceof Player.MCTSPlayer) {
            Player.MCTSPlayer mp = (Player.MCTSPlayer) player;
            while (true) {
                Action a = mp.decide(deck, dealer);
                if (a == Action.Hit) {
                    mp.hit(deck);
                    if (mp.isBusted()) return Result.LOSE;
                } else {
                    break;
                }
            }
        } else {
            while (player.action(deck) == Action.Hit) {
                if (player.isBusted()) return Result.LOSE;
            }
        }

        // 庄家回合
        while (true) {
            Action a = dealer.action(deck);
            if (a == Action.Hit) {
                // DealerPlayer.action 已经打牌
                if (dealer.isBusted()) return Result.WIN;
            } else {
                break;
            }
        }

        // 最后比点
        int ps = player.score(), ds = dealer.score();
        return ps > ds ? Result.WIN : ps < ds ? Result.LOSE : Result.DRAW;
    }
}