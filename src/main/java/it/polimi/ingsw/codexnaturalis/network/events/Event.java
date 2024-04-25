package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serializable;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public abstract class Event implements Serializable {

    // TODO: @Serial

    public Event() {
    }

    public void run(ControllerState controller) throws IllegalCommandException {
    }
}