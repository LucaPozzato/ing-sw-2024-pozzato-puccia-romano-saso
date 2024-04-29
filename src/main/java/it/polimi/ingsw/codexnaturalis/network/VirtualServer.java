package it.polimi.ingsw.codexnaturalis.network;

import java.rmi.Remote;
import java.rmi.RemoteException;

import it.polimi.ingsw.codexnaturalis.network.commands.Command;

public interface VirtualServer extends Remote {
    void connect(VirtualClient client) throws RemoteException;

    void queueUpdate(Command command) throws IllegalStateException;
}
