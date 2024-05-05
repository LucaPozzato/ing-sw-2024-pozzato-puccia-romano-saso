package it.polimi.ingsw.codexnaturalis.network.server;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.utils.DefaultValue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SocketServer implements VirtualServer, Runnable {

    private final ServerSocket serverSocket;
    final ControllerState controller;
    final List<VirtualClient> clients;
    private final Queue<Command> commandEntryQueue;
    private final Queue<Event> eventExitQueue;


    public SocketServer(ControllerState controller) {

        try {
            this.serverSocket = new ServerSocket(DefaultValue.port_Socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        this.controller = controller;
        this.clients = new ArrayList<>();
        this.commandEntryQueue = new LinkedList<Command>();
        this.eventExitQueue = new LinkedList<Event>();
    }

    @Override
    public void receiveCommand (Command command) throws IllegalStateException {
        commandEntryQueue.add(command);
        notifyAll();
    }

    public void processCommandThread() {
        new Thread(this::processCommand).start();
    }

    public void processCommand() {
        // TODO: gestione eccezioni
        while (true) {
            try {
                Command command = this.commandEntryQueue.poll();
                synchronized (this.controller) {
                    assert command != null;
                    command.execute(controller);
                }
            } catch (Exception e) {
                // System.err.println("");
                break;
            }
        }
    }

    @Override
    public void sendEvent (Event event) throws IllegalStateException {
        eventExitQueue.add(event);
        notifyAll();
        // gestione exception?
    }

    public void processEventThread() {
        new Thread(this::processEvent).start();
    }

    public void processEvent() {
        // TODO: gestione eccezioni
        while (true) {
            try {
                Event event = this.eventExitQueue.poll();
                synchronized (this.clients) {
                    for (var client : this.clients) {
                        client.receiveEvent(event);
                    }
                }
            } catch (Exception e) {
                // System.err.println("");
                break;
            }
        }
    }

    @Override
    public void connect(VirtualClient client) {
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }

    public void disconnectClient(VirtualClient client) {
        synchronized (clients) {
            if (clients.remove(client)) {
                if (client instanceof SocketSkeleton) {
                    ((SocketSkeleton) client).stop();
                }
            }
        }
    }

    public void run() {
        try {
            Socket socket = null;
            while (!Thread.interrupted()) {
                socket = serverSocket.accept();
                SocketSkeleton client = new SocketSkeleton(this, socket);
                clients.add(client);
                new Thread(client).start();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
