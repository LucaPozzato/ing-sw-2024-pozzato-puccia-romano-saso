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
            assertEquals("Cannot add more than 4 cards", e.getMessage());
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
            assertEquals("Card is not in hand", e.getMessage());
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
        // [x] Cannot remove more than 4 cards
        // [x] Cannot remove card that is not present
    }

    @Test
    void testUpdateActualScore() {

        // [ ] adds the point
    }

    @Test
    void testUpdateVirtualScore() {

    }
}
