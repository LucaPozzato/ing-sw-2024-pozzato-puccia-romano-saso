package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;
import java.io.Serializable;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

/**
 * Events are created by the Model in order to communicate its changes to the
 * clients
 * they are later passed to the server, sent in broadcast to the clients through
 * the network
 * the clients will call the doJob method, passing the minimodel
 */
public abstract class Event implements Serializable {
    @Serial
    private static final long serialVersionUID = 490127365189273L;

    public Event() {
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
    }

    public String getClientId() {
        return null;
    }

    public int getGameId() {
        return 0;
    }

}