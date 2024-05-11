package it.polimi.ingsw.codexnaturalis.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;

public interface VirtualClient extends Remote {

    void receiveEvent(Event event) throws RemoteException;

    void sendCommand(Command command) throws RemoteException;

    String getClientId() throws RemoteException;
    String getPassword() throws RemoteException;
    void setPassword(String password) throws RemoteException;

    void ping() throws RemoteException;
}
