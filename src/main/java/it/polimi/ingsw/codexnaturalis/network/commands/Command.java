package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serial;
import java.io.Serializable;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public abstract class Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 380164927530291L;

    public Command() {
    }

    public void execute(ControllerState controller) throws IllegalCommandException{
    }
}