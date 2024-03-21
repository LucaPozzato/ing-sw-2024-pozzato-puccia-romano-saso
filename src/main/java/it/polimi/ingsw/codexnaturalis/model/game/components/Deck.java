package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.util.Collections;
import java.util.Stack;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.*;

public class Deck {
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

    public void addGoldCard(GoldCard card) {
        this.goldDeck.push(card);
    }

    public void addResourceCard(ResourceCard card) {
        this.resourceDeck.push(card);
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

    public boolean emptyGold() {
        return goldDeck.isEmpty();
    }

    public boolean emptyRes() {
        return resourceDeck.isEmpty();
    }

}
