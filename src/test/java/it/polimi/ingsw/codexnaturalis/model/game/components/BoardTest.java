package it.polimi.ingsw.codexnaturalis.model.game.components;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Stack;

import org.junit.jupiter.api.Test;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.parser.InitialParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ObjectiveParser;
import it.polimi.ingsw.codexnaturalis.model.game.parser.ResourceParser;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class BoardTest {
    @Test
    void testAddUncoveredCard() {
        Board board = new Board();
        Stack<ResourceCard> resourceDeck = new ResourceParser().parse();
        InitialCard initialCard = new InitialParser().parse().get(0);
        ObjectiveCard objectiveCard = new ObjectiveParser().parse().get(0);

        try {
            board.addUncoveredCard(initialCard);
        } catch (Exception e) {
            assertFalse(board.getUncoveredCards().contains(initialCard));
            assertEquals("Cannot add card, wrong card type", e.getMessage());
        }

        try {
            board.addUncoveredCard(objectiveCard);
        } catch (Exception e) {
            assertFalse(board.getUncoveredCards().contains(objectiveCard));
            assertEquals("Cannot add card, wrong card type", e.getMessage());
        }

        try {
            for (int i = 0; i < 4; i++) {
                board.addUncoveredCard(resourceDeck.get(i));
                assertTrue(board.getUncoveredCards().contains(resourceDeck.get(i)));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            board.addUncoveredCard(resourceDeck.get(4));
        } catch (Exception e) {
            assertFalse(board.getUncoveredCards().contains(resourceDeck.get(4)));
            assertEquals("Board is full", e.getMessage());
        }

        // [x] Adds card
        // [x] Cannot add more than 4 cards
    }

    @Test
    void testRemoveUncoveredCard() {
        Board board = new Board();
        Stack<ResourceCard> resourceDeck = new ResourceParser().parse();

        try {
            for (int i = 0; i < 4; i++) {
                board.addUncoveredCard(resourceDeck.get(i));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            assertFalse(board.getUncoveredCards().contains(resourceDeck.get(4)));
            board.removeUncoveredCard(resourceDeck.get(4));
        } catch (Exception e) {
            assertEquals("Card is not in board", e.getMessage());
        }

        try {
            board.removeUncoveredCard(resourceDeck.get(0));
            assertFalse(board.getUncoveredCards().contains(resourceDeck.get(0)));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        try {
            board.removeUncoveredCard(resourceDeck.get(1));
        } catch (Exception e) {
            assertTrue(board.getUncoveredCards().contains(resourceDeck.get(1)));
            assertEquals("Cannot remove more than one card", e.getMessage());
        }

        // [x] Removes card
        // [x] Cannot remove more than 1 card
        // [x] Cannot remove card that is not present
    }

    @Test
    void testUpdateActualScore() {
        Board board = new Board();
        Player testPlayer = new Player("player1");
        String[] actualScores;

        // test that verifies player are added with 0 points
        try {
            board.updateActualScore(testPlayer, 0);
            for (int i = 0; i < 3; i++) {
                board.updateActualScore(new Player("player" + (i + 2)), i + 1);
            }
            actualScores = board.drawActualScores().split(" \\| ");
            for (String score : actualScores) {
                for (int j = 0; j < 4; j++) {
                    if (score.contains("player" + j)) {
                        assertEquals("player" + j + ": 0", score);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // test that verifies points are added correctly
        try {
            board.updateActualScore(testPlayer, 1);
            actualScores = board.drawActualScores().split(" \\| ");
            for (String score : actualScores) {
                if (score.contains("player1")) {
                    assertEquals("player1: 1", score);
                    break;
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // test that verifies exception is thrown if more than 4 players
        try {
            board.updateActualScore(new Player("player5"), 0);
        } catch (Exception e) {
            assertEquals("Cannot have more than 4 players", e.getMessage());
        }

        // [x] adds the point
        // [x] adds the player with 0 points
        // [x] throws exception if more than 4 players
    }

    @Test
    void testUpdateVirtualScore() {
        Board board = new Board();
        Player testPlayer = new Player("player1");
        String[] virtualScores;

        // test that verifies player are added with 0 points
        try {
            board.updateVirtualScore(testPlayer, 0);
            for (int i = 0; i < 3; i++) {
                board.updateVirtualScore(new Player("player" + (i + 2)), i + 1);
            }
            virtualScores = board.drawVirtualScores().split(" \\| ");
            for (String score : virtualScores) {
                for (int j = 0; j < 4; j++) {
                    if (score.contains("player" + j)) {
                        assertEquals("player" + j + ": 0", score);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // test that verifies points are added correctly
        try {
            board.updateVirtualScore(testPlayer, 1);
            virtualScores = board.drawVirtualScores().split(" \\| ");
            for (String score : virtualScores) {
                if (score.contains("player1")) {
                    assertEquals("player1: 1", score);
                    break;
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // test that verifies exception is thrown if more than 4 players
        try {
            board.updateVirtualScore(new Player("player5"), 0);
        } catch (Exception e) {
            assertEquals("Cannot have more than 4 players", e.getMessage());
        }

        // [x] adds the point
        // [x] adds the player with 0 points
        // [x] throws exception if more than 4 players
    }
}
