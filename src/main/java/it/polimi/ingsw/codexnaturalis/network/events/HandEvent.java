package it.polimi.ingsw.codexnaturalis.network.events;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.network.MiniModel;

public class HandEvent extends Event {
    List<Hand> hands;

    public HandEvent(List<Hand> hands) {
        this.hands = hands;
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setHands(hands);
    }
}