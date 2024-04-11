package it.polimi.ingsw.codexnaturalis.network.Socket;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualView {
    public void showValue(Integer number);

    public void reportError(String details);
}
