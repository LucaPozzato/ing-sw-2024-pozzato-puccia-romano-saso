package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serial;
import java.io.Serializable;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

/**
 * Commands are created by the view according to the clients' input
 * they are later passed to the client, sent to the server through the network
 * which will call the execute method, passing the controller
 */
public abstract class Command implements Serializable {
    @Serial
    private static final long serialVersionUID = 380164927530291L;

    public Command() {
    }

    public void execute(ControllerState controller) throws IllegalCommandException {
    }

    public int getGameId() {
        return 0;
    }

    public String getClientId() {
        return null;
    }

    public String getPassword() {
        return null;
    }
}