package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class HandEvent extends Event {
    @Serial
    private static final long serialVersionUID = 740215983672910L;
    List<Hand> hands;

    public HandEvent(List<Hand> hands) {
        this.hands = hands;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setHands(hands);
    }
}