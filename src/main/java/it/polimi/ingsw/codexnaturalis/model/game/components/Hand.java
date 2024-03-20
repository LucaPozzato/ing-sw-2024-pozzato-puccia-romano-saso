package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;

public class Hand {

    private List<Card> chooseBetweenObj;
    private Card secretObjective;
    private List<Card> cardsHand;

    // costruttore
    public Hand() {
    }

    public void setSecretObjective(Card secretObjective) {
        this.secretObjective = secretObjective;
    }

    // getter
    public Card getSecretObjective() {
        return secretObjective;
    }

    public List<Card> getCardsHand() {
        return cardsHand;
    }

    public void addCard(Card card) {
        cardsHand.add(card);
    }

    public void removeCard(Card card) {
        cardsHand.remove(card);
    }

}
