package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;

/**
 * Represents the two decks of cards used in the game, the gold deck and the
 * resource deck.
 * Implements Serializable to support object serialization.
 */
public class Deck implements Serializable {
    @Serial
    private static final long serialVersionUID = 928375019264738L;

    private Stack<GoldCard> goldDeck;
    private Stack<ResourceCard> resourceDeck;

    /**
     * Constructs the two decks.
     *
     * @param goldDeck     The stack of gold cards to initialize the deck.
     * @param resourceDeck The stack of resource cards to initialize the deck.
     */

    public Deck(Stack<GoldCard> goldDeck, Stack<ResourceCard> resourceDeck) {
        this.goldDeck = goldDeck;
        this.resourceDeck = resourceDeck;
    }

    public Stack<GoldCard> getGoldDeck() {
        return goldDeck;
    }

    public Stack<ResourceCard> getResourceDeck() {
        return resourceDeck;
    }

    public void addGoldCard(GoldCard card) {
        this.goldDeck.push(card);
    }

    public void addResourceCard(ResourceCard card) {
        this.resourceDeck.push(card);
    }

    /**
     * Draws (removes and returns) a gold card from the top of the deck.
     *
     * @return The gold card drawn from the deck.
     */

    public GoldCard drawGoldCard() {
        return goldDeck.pop();
    }

    /**
     * Draws (removes and returns) a resource card from the top of the deck.
     *
     * @return The resource card drawn from the deck.
     */
    public ResourceCard drawResourceCard() {
        return resourceDeck.pop();
    }

    /**
     * Shuffles the stack of gold cards in the deck.
     */
    public void shuffleGoldDeck() {
        Collections.shuffle(goldDeck);
    }

    /**
     * Shuffles the stack of resource cards in the deck.
     */
    public void shuffleResourceDeck() {
        Collections.shuffle(resourceDeck);
    }
}
