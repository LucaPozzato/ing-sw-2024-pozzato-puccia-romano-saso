package it.polimi.ingsw.codexnaturalis.network.RMI;

import it.polimi.ingsw.codexnaturalis.controller.ServerController;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class Server implements VirtualServer {

    final ServerController controller;
    final List<VirtualView> clients = new ArrayList<>(); // qui dentro ci sono gli stub dei clients

    public Server(ServerController controller) {
        this.controller = controller;
    }

    // metodi controller

    public static void main(String[] args) throws RemoteException {
        final String serverName = "Server";

        VirtualServer server = new Server(new ServerController());
        VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(server, 0); // Unicast: comunicazione uno
                                                                                          // a uno con il componente
        Registry registry = LocateRegistry.createRegistry(1234);
        registry.rebind(serverName, stub); // rebind.. se esiste sovrascrive. Possiamo usare anche bind
    }

    @Override
    public void connect(VirtualView client) throws RemoteException {
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }
}