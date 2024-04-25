package it.polimi.ingsw.codexnaturalis.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {

    void connect(VirtualClient client) throws RemoteException;
    // metodo per segnalare al server chi siamo, per contattarci invece du throws
    // dobbiamo implementare try catch

    // Qui inseriamo i metodi che il client vuole usare
}
