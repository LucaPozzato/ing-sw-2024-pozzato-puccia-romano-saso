package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;

public class Hand {

    private List<Card> chooseBetweenObj;
    private Card secretObjective;
    private List<Card> cardsHand;
    private InitialCard initCard;

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

    public List<Card> getChooseBetweenObj() {
        return chooseBetweenObj;
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

    public void setInitCard(InitialCard initCard) {
        this.initCard = initCard;
    }

}
