package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import javafx.util.Pair;

public class Hand implements Serializable {
    @Serial
    private static final long serialVersionUID = 503921874926310L;

    private List<Card> chooseBetweenObj;
    private Card secretObjective;
    private List<Pair<Card, Boolean>> cardsHand;
    private InitialCard initCard;
    private int emptyIndex = 0;

    // costruttore
    public Hand() {
        cardsHand = new ArrayList<>();
        chooseBetweenObj = new ArrayList<>();
    }

    /**
     * Sets the secret objective card
     * 
     * @param secretObjective the secret objective card
     */
    public void setSecretObjective(Card secretObjective) {
        this.secretObjective = secretObjective;
    }

    /**
     * Returns the secret objective card
     * 
     * @return the secret objective card
     */
    public Card getSecretObjective() {
        return this.secretObjective;
    }

    /**
     * Sets the objective cards to choose from
     * 
     * @param chooseBetweenObj the objective cards to choose from
     */
    public void setChooseBetweenObj(List<Card> chooseBetweenObj) {
        this.chooseBetweenObj = chooseBetweenObj;
    }

    /**
     * Returns the objective cards to choose from
     * 
     * @return the objective cards to choose from
     */
    public List<Card> getChooseBetweenObj() {
        return this.chooseBetweenObj;
    }

    /**
     * Returns the cards in the hand
     * 
     * @return the cards in the hand
     */
    public List<Card> getCardsHand() {
        List<Card> tempList = new ArrayList<>();
        for (Pair<Card, Boolean> pair : cardsHand) {
            tempList.add(pair.getKey());
        }
        return tempList;
    }

    /**
     * Sets the initial card
     * 
     * @param initCard the initial card
     */
    public void setInitCard(InitialCard initCard) {
        this.initCard = initCard;
    }

    /**
     * Returns the initial card
     * 
     * @return the initial card
     */
    public InitialCard getInitCard() {
        return this.initCard;
    }

    /**
     * Adds a card to the hand
     * 
     * @param card the card to add
     * @throws IllegalCommandException if the card is not of the right type or the
     *                                 hand is full
     */
    public void addCard(Card card) throws IllegalCommandException {
        Pair<Card, Boolean> pair = new Pair<>(card, true);

        if (card instanceof InitialCard || card instanceof ObjectiveCard)
            throw new IllegalCommandException("Cannot add card, wrong card type");

        switch (this.cardsHand.size()) {
            case 0, 1:
                this.cardsHand.add(emptyIndex, pair);
                emptyIndex++;
                break;
            case 2:
                this.cardsHand.add(emptyIndex, pair);
                break;
            case 3:
                throw new IllegalCommandException("Hand is full");
            default:
                break;
        }
    }

    /**
     * Removes a card from the hand
     * 
     * @param card the card to remove
     * @throws IllegalCommandException if the card is not in the hand
     */
    public void removeCard(Card card) throws IllegalCommandException {
        if (cardsHand.size() < 3)
            throw new IllegalCommandException("Cannot remove more than one card");

        Boolean contained = false;

        for (int i = 0; i < 3; i++) {
            if (cardsHand.get(i).getKey().equals(card)) {
                contained = true;
                break;
            }
        }
        if (!contained)
            throw new IllegalCommandException("Card is not in hand");

        for (Pair<Card, Boolean> pair : cardsHand) {
            if (pair.getKey().equals(card)) {
                emptyIndex = cardsHand.indexOf(pair);
                this.cardsHand.remove(pair);
                break;
            }
        }
    }
}
