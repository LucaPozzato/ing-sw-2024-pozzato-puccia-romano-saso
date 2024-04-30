package it.polimi.ingsw.codexnaturalis.network;

import it.polimi.ingsw.codexnaturalis.network.commands.Command;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualClientHandler extends Remote {
    public void queueUpdate(Command command) throws RemoteException;

    void updateQueue(Command command);
}
