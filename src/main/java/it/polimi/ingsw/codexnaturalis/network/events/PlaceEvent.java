package it.polimi.ingsw.codexnaturalis.network.events;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.network.MiniModel;

public class PlaceEvent extends Event {
    List<Structure> playerStructure;

    public PlaceEvent(List<Structure> playerStructure) {
        this.playerStructure = playerStructure;
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setPlayerStructure(playerStructure);
    }
}