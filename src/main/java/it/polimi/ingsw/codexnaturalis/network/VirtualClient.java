package it.polimi.ingsw.codexnaturalis.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;

/**
 * Remote interface representing a virtual client in the network.
 */
public interface VirtualClient extends Remote {

    void receiveEvent(Event event) throws RemoteException;

    void sendCommand(Command command) throws RemoteException;

    String getClientId() throws RemoteException;

    void ping() throws RemoteException;
}
