package it.polimi.ingsw.codexnaturalis.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;

/**
 * Remote interface representing a virtual server in the network.
 */
public interface VirtualServer extends Remote {

    void connect(VirtualClient client) throws RemoteException;

    void receiveCommand(Command command) throws RemoteException;

    void sendEvent(Event event) throws RemoteException;
}
