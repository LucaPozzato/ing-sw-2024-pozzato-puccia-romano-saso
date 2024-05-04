package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;
import java.io.Serializable;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public abstract class Event implements Serializable {
    @Serial
    private static final long serialVersionUID = 490127365189273L;

    public Event() {
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
    }
}