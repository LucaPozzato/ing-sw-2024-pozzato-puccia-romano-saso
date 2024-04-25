package it.polimi.ingsw.codexnaturalis.network.RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import it.polimi.ingsw.codexnaturalis.view.View;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.view.GameCli;
// import it.polimi.ingsw.codexnaturalis.view.GameGui;

public class Client extends UnicastRemoteObject implements VirtualClient {
    final VirtualServer server;
    private boolean isCli;
    private View view;

    public Client(VirtualServer server, boolean isCli) throws RemoteException {
        this.server = server;
        this.isCli = isCli;
    }

    private void run() throws RemoteException {
        this.server.connect(this);
        if (isCli)
            runCli();
    }

    private void runCli() throws RemoteException {
        // this.view = new GameCli();
        // [...]
    }

    private void runGui() throws RemoteException {
        // this.view = new GameGui();
        // [...]
    }

    @Override
    public void updateClient(Event event) {
        event = null;
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        final String serverName = "Server";
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);

        VirtualServer server = (VirtualServer) registry.lookup(serverName);
        Boolean isCli = true;
        // TODO: read isCli

        new Client(server, isCli).run();
    }
}
