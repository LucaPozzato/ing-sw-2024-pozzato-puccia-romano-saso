package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serializable;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public abstract class Command implements Serializable {

    // TODO: @Serial

    public Command() {
    }

    public void execute(ControllerState controller) throws IllegalCommandException {
    }
}