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

public class HandTest {
    @Test
    void testAddCard() {
        Hand hand = new Hand();
        Stack<ResourceCard> resourceDeck = new ResourceParser().parse();
        InitialCard initialCard = new InitialParser().parse().get(0);
        ObjectiveCard objectiveCard = new ObjectiveParser().parse().get(0);

        // test that verifies that cannot add wrong card type
        try {
            hand.addCard(initialCard);
        } catch (Exception e) {
            assertFalse(hand.getCardsHand().contains(initialCard));
            assertEquals("Cannot add card, wrong card type", e.getMessage());
        }

        // test that verifies that cannot add wrong card type
        try {
            hand.addCard(objectiveCard);
        } catch (Exception e) {
            assertFalse(hand.getCardsHand().contains(objectiveCard));
            assertEquals("Cannot add card, wrong card type", e.getMessage());
        }

        // test that verifies that the cards are added correctly
        try {
            for (int i = 0; i < 3; i++) {
                hand.addCard(resourceDeck.get(i));
                assertTrue(hand.getCardsHand().contains(resourceDeck.get(i)));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // test that verifies that cannot add more than 4 cards
        try {
            hand.addCard(resourceDeck.get(3));
        } catch (Exception e) {
            assertFalse(hand.getCardsHand().contains(resourceDeck.get(4)));
            assertEquals("Hand is full", e.getMessage());
        }

        // [x] Adds card
        // [x] Cannot add more than 4 cards
    }

    @Test
    void testRemoveCard() {
        Hand hand = new Hand();
        Stack<ResourceCard> resourceDeck = new ResourceParser().parse();

        // initialized hand with cards
        try {
            for (int i = 0; i < 3; i++) {
                hand.addCard(resourceDeck.get(i));
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // test that verifies that card is not in hand
        try {
            assertFalse(hand.getCardsHand().contains(resourceDeck.get(4)));
            hand.removeCard(resourceDeck.get(4));
        } catch (Exception e) {
            assertEquals("Card is not in hand", e.getMessage());
        }

        // test that verifies that card is removed correctly
        try {
            hand.removeCard(resourceDeck.get(0));
            assertFalse(hand.getCardsHand().contains(resourceDeck.get(0)));
        } catch (Exception e) {
            fail(e.getMessage());
        }

        // test that verifies that cannot remove more than 1 cards
        try {
            hand.removeCard(resourceDeck.get(1));
        } catch (Exception e) {
            assertTrue(hand.getCardsHand().contains(resourceDeck.get(1)));
            assertEquals("Cannot remove more than one card", e.getMessage());
        }

        // [x] Removes card
        // [x] Cannot remove more than 4 cards
        // [x] Cannot remove card that is not present
    }
}
