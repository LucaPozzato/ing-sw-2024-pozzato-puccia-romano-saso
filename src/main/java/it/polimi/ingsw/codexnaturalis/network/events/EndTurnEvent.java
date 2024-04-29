package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.network.MiniModel;

public class EndTurnEvent extends Event {
    private Integer turnCounter;
    private boolean lastTurn;

    public EndTurnEvent(Integer turnCounter, boolean lastTurn) {
        this.lastTurn = lastTurn;
        this.turnCounter = turnCounter;
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setLastTurn(lastTurn);
        miniModel.setTurnCounter(turnCounter);
    }

}