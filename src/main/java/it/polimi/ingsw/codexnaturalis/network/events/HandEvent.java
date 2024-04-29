package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.network.MiniModel;

public class HandEvent extends Event {
    Hand hand;

    public HandEvent(Hand hand) {
        this.hand = hand;
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setHand(hand);
    }
}