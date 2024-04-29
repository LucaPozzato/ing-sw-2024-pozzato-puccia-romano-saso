package it.polimi.ingsw.codexnaturalis.network.RMI;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;

import it.polimi.ingsw.codexnaturalis.network.MiniModel;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.view.View;

public class Client extends UnicastRemoteObject implements VirtualClient {
    final VirtualServer server;
    private boolean isCli;
    private View view;
    private MiniModel miniModel;
    private final Queue<Event> eventQueue;

    public Client(VirtualServer server, boolean isCli) throws RemoteException {
        this.server = server;
        this.isCli = isCli;
        this.eventQueue = new LinkedList<Event>();

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

    // function that sends our command to the server
    // so far the command is to be created in the view, which notifies the client,
    // which sends it to the server
    public void sendCommand(Command command) {
        server.queueUpdate(command);
    }

    // function that recieves the event from the server and adds it to the queue
    @Override
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
                synchronized (this.miniModel) {
                    event.doJob(miniModel);
                }
            } catch (Exception e) {
                // System.err.println("");
                break;
            }
        }
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {

        // connessione RMI
        final String serverName = "Server";
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);

        VirtualServer server = (VirtualServer) registry.lookup(serverName);
        Boolean isCli = true;
        // TODO: read isCli

        new Client(server, isCli).run();
    }
}
