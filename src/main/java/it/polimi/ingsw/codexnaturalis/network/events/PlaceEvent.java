package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class PlaceEvent extends Event {
    @Serial
    private static final long serialVersionUID = 693215087341982L;
    private String state;
    List<Structure> playerStructure;
    List<Hand> hands;

    public PlaceEvent(String state, List<Structure> playerStructure, List<Hand> hands) {
        this.state = state;
        this.playerStructure = playerStructure;
        this.hands = hands;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setPlayerStructure(playerStructure);
        miniModel.setHands(hands);
        miniModel.setState(state);
    }
}