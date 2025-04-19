package com.phasmidsoftware.dsaipg.projects.mcts.blackjackMCTS;

import com.phasmidsoftware.dsaipg.projects.mcts.blackjack.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MCTSNodeTest {

    @Test
    void testIsFullyExpandedFalseWhenNoChildren() {
        MCTSNode node = createEmptyNode();
        assertFalse(node.isFullyExpanded());
    }

    @Test
    void testIsFullyExpandedFalseWhenOneChild() {
        MCTSNode node = createEmptyNode();
        node.children.add(createEmptyNode());
        assertFalse(node.isFullyExpanded());
    }

    @Test
    void testIsFullyExpandedTrueWhenTwoChildren() {
        MCTSNode node = createEmptyNode();
        node.children.add(createEmptyNode());
        node.children.add(createEmptyNode());
        assertTrue(node.isFullyExpanded());
    }

    @Test
    void testGetUCTValueWhenUnvisited() {
        MCTSNode parent = createEmptyNode();
        parent.visits = 10;

        MCTSNode node = new MCTSNode(
                new Player.RandomPlayer(),
                new DealerPlayer(new Player.RandomPlayer()),
                new Deck(),
                Action.Hit,
                parent
        );

        // visits = 0 → expect MAX
        assertEquals(Double.MAX_VALUE, node.getUCTValue());
    }

    @Test
    void testGetUCTValueNormalCase() {
        MCTSNode parent = createEmptyNode();
        parent.visits = 10;

        MCTSNode node = new MCTSNode(
                new Player.RandomPlayer(),
                new DealerPlayer(new Player.RandomPlayer()),
                new Deck(),
                Action.Stand,
                parent
        );
        node.visits = 5;
        node.wins = 3;

        double uct = node.getUCTValue();

        // 检查大概范围
        assertTrue(uct > 0.5 && uct < 2.0, "UCT value should be within expected range");
    }

    // 工具函数快速创建空节点
    private MCTSNode createEmptyNode() {
        return new MCTSNode(
                new Player.RandomPlayer(),
                new DealerPlayer(new Player.RandomPlayer()),
                new Deck(),
                null,
                null
        );
    }
}
