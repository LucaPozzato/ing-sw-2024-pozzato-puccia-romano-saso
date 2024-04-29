package it.polimi.ingsw.codexnaturalis.network.RMI;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.controller.InitState;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;

public class Server implements VirtualServer {
    final ControllerState controller;
    final List<VirtualClient> clients = new ArrayList<>(); // qui dentro ci sono gli stub dei clients
    private final Queue<Command> commandQueue;

    public Server(ControllerState controller) {
        this.controller = controller;
        this.commandQueue = new LinkedList<Command>();
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

    @Override
    public void queueUpdate(Command command) throws IllegalStateException {
        commandQueue.add(command);
        // notify del thread;
        // gestione exception?
    }

    public void processCommandThread() {
        new Thread(this::processCommand).start();
    }

    public void processCommand() {
        // TODO: gestione eccezioni
        while (true) {
            try {
                Command command = this.commandQueue.poll();
                synchronized (this.controller) {
                    command.execute(controller);
                }
            } catch (Exception e) {
                // System.err.println("");
                break;
            }
        }
    }
}