package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Stack;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;

public class Deck implements Serializable {
    @Serial
    private static final long serialVersionUID = 928375019264738L;

    private Stack<GoldCard> goldDeck;
    private Stack<ResourceCard> resourceDeck;

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

    public GoldCard drawGoldCard() {
        return goldDeck.pop();
    }

    public ResourceCard drawResourceCard() {
        return resourceDeck.pop();
    }

    public void shuffleGoldDeck() {
        Collections.shuffle(goldDeck);
    }

    public void shuffleResourceDeck() {
        Collections.shuffle(resourceDeck);
    }
}
