package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Printer;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import javafx.util.Pair;

public class Hand {
    private List<Card> chooseBetweenObj;
    private Card secretObjective;
    private List<Pair<Card, Boolean>> cardsHand;
    private InitialCard initCard;
    private List<String> visualHand;
    int emptyIndex = 0;
    Boolean full;

    // costruttore
    public Hand() {
        cardsHand = new ArrayList<>();
        visualHand = new ArrayList<>();
        chooseBetweenObj = new ArrayList<>();
    }

    public void setSecretObjective(Card secretObjective) {
        this.secretObjective = secretObjective;
    }

    public void setInitCard(InitialCard initCard) {
        this.initCard = initCard;
    }

    // getter
    public Card getSecretObjective() {
        return this.secretObjective;
    }

    public List<Card> getChooseBetweenObj() {
        return this.chooseBetweenObj;
    }

    public List<Card> getCardsHand() {
        List<Card> tempList = new ArrayList<>();
        for (Pair<Card, Boolean> pair : cardsHand) {
            tempList.add(pair.getKey());
        }
        return tempList;
    }

    public InitialCard getInitCard() {
        return this.initCard;
    }

    public void addCard(Card card) throws IllegalCommandException {
        Pair<Card, Boolean> pair = new Pair<>(card, true);

        if (card instanceof InitialCard || card instanceof ObjectiveCard)
            throw new IllegalCommandException("Cannot add card, wrong card type");

        switch (this.cardsHand.size()) {
            case 0, 1:
                this.cardsHand.add(emptyIndex, pair);
                this.visualHand.add(emptyIndex, card.drawDetailedVisual(true));
                emptyIndex++;
                break;
            case 2:
                this.cardsHand.add(emptyIndex, pair);
                this.visualHand.add(emptyIndex, card.drawDetailedVisual(true));
                break;
            case 3:
                throw new IllegalCommandException("Hand is full");
            default:
                break;
        }
    }

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
                this.visualHand.remove(emptyIndex);
                this.cardsHand.remove(pair);
                break;
            }
        }
    }

    public List<String> drawCardsHand() throws IllegalCommandException {
        return new Printer().printHand(visualHand, cardsHand);
    }

    public String drawSecretObjective() throws IllegalCommandException {
        return secretObjective.drawDetailedVisual(true);
    }

    public List<String> drawChooseBetweenObj() throws IllegalCommandException {
        return new ArrayList<>(List.of(chooseBetweenObj.get(0).drawDetailedVisual(true),
                chooseBetweenObj.get(1).drawDetailedVisual(true)));
    }
}
