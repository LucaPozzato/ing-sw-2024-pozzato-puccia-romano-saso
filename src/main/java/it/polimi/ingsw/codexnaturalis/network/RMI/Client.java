package it.polimi.ingsw.codexnaturalis.network.RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class Client extends UnicastRemoteObject implements VirtualView {

    final VirtualServer server;

    public Client(VirtualServer server) throws RemoteException {
        this.server = server;
    }

    private void run() throws RemoteException {
        this.server.connect(this);
        this.runCli();
    }

    private void runCli() throws RemoteException {

    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final String serverName = "Server";
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);

        VirtualServer server = (VirtualServer) registry.lookup(serverName);

        new Client(server).run();

    }
}
