package it.polimi.ingsw.codexnaturalis.network;

import java.rmi.Remote;

import it.polimi.ingsw.codexnaturalis.network.events.Event;

public interface VirtualClient extends Remote {

    public void queueUpdate(Event event) throws IllegalStateException;
}
