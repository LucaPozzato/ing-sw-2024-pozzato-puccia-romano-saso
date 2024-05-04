package it.polimi.ingsw.codexnaturalis.network;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;

public interface VirtualClient extends Remote, Serializable {

    void receiveEvent(Event event) throws RemoteException;
    void sendCommand (Command command) throws RemoteException;
}
