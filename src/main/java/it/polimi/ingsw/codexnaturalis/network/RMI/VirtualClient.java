package it.polimi.ingsw.codexnaturalis.network.RMI;

import java.rmi.Remote;

import it.polimi.ingsw.codexnaturalis.network.events.Event;

public interface VirtualClient extends Remote {
    // SKELETON

    public void updateClient(Event event);
    // Metodi che il client vuole ricevere: es "showUpdate"
}
