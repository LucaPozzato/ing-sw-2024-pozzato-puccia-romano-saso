package it.polimi.ingsw.codexnaturalis.network.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.controller.InitState;
import it.polimi.ingsw.codexnaturalis.model.game.Game;

public class Server implements VirtualServer {
    final ControllerState controller;
    final List<VirtualClient> clients = new ArrayList<>(); // qui dentro ci sono gli stub dei clients
    private final Queue<Event> eventQueue;

    public Server(ControllerState controller) {
        this.controller = controller;
        this.eventQueue = new LinkedList<Event>();
    }

    public static void main(String[] args) throws RemoteException {
        try {
            final String serverName = "Server";

            VirtualServer server = new Server(new InitState(new Game(0)) {
            });
            VirtualServer stub = (VirtualServer) UnicastRemoteObject.exportObject(server, 0);
            Registry registry = LocateRegistry.createRegistry(1234);
            registry.rebind(serverName, stub); // rebind.. se esiste sovrascrive. Possiamo usare anche bind
            System.out.println("Server pronto...");
        } catch (Exception e) {
            System.err.println("Server exception: " + e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public void connect(VirtualClient client) throws RemoteException {
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }

    public void queueUpdate(Event event) throws IllegalStateException {
        eventQueue.add(event);
        // notify del thread;
        // gestione exception?
    }

    public void processEventThread() {
        new Thread(this::processEvent).start();
    }

    public void processEvent() {
        // TODO: gestione eccezioni
        while (true) {
            try {
                Event event = this.eventQueue.poll();
                synchronized (this.controller) {
                    event.run(controller);
                }
            } catch (Exception e) {
                // System.err.println("");
                break;
            }
        }
    }
}