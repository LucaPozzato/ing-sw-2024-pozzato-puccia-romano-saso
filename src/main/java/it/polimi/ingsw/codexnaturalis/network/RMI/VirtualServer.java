package it.polimi.ingsw.codexnaturalis.network.RMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualServer extends Remote {

    void connect(VirtualView client) throws RemoteException; // metodo per segnalare al server chi siamo, per
                                                             // contattarcu
                                                             // invece du throws dobbiamo implementare try catch

    // Qui inseriamo i metodi che il client vuole usare
}
