package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Printer;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import javafx.util.Pair;

public class Hand {
    private List<Card> chooseBetweenObj;
    private Card secretObjective;
    private List<Pair<Card, Boolean>> cardsHand;
    private InitialCard initCard;
    private List<String> visualHand;
    int emptyPlace = 0;
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

        switch (this.cardsHand.size()) {
            case 0, 1:
                this.cardsHand.add(emptyPlace, pair);
                this.visualHand.add(emptyPlace, card.drawDetailedVisual(true));
                emptyPlace++;
                break;
            case 2:
                this.cardsHand.add(emptyPlace, pair);
                this.visualHand.add(emptyPlace, card.drawDetailedVisual(true));
                break;
            case 3:
                throw new IllegalCommandException("Hand is full");
            default:
                break;
        }
    }

    public void removeCard(Card card) {
        for (Pair<Card, Boolean> pair : cardsHand) {
            if (pair.getKey().equals(card)) {
                emptyPlace = cardsHand.indexOf(pair);
                this.visualHand.remove(emptyPlace);
                this.cardsHand.remove(pair);
                break;
            }
        }
    }

    public void flipCard(Card card) throws IllegalCommandException {
        int i = 0;
        for (Pair<Card, Boolean> pair : cardsHand) {
            if (pair.getKey().equals(card)) {
                break;
            }
            i++;
        }
        visualHand.add(i, card.drawDetailedVisual(!cardsHand.get(i).getValue()));
    }

    public void flipAll() throws IllegalCommandException {
        for (int i = 0; i < cardsHand.size(); i++) {
            flipCard(cardsHand.get(i).getKey());
        }
    }

    public List<String> drawCardsHand() throws IllegalCommandException {
        return new Printer().printHand(visualHand, cardsHand);
    }

    public String drawSecretObjective() throws IllegalCommandException {
        return secretObjective.drawDetailedVisual(true);
    }
}
