package it.polimi.ingsw.codexnaturalis.network.events;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.network.MiniModel;

public class DrawEvent extends Event {
    private List<Card> uncoveredCards;
    private Integer turnCounter;

    public DrawEvent(Hand hand, List<Card> uncoveredCards, Integer turnCounter) {
        this.uncoveredCards = uncoveredCards;
        this.turnCounter = turnCounter;
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setUncoveredCards(uncoveredCards);
        miniModel.setTurnCounter(turnCounter);
    }

}