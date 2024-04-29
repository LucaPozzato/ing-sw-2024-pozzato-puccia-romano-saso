package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serializable;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.network.MiniModel;

public abstract class Event implements Serializable {

    // TODO: @Serial

    public Event() {
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
    }
}