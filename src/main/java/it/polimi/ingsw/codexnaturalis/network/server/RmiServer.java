package it.polimi.ingsw.codexnaturalis.network.server;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RmiServer implements VirtualServer {
    private ControllerState controller;
    private List<VirtualClient> clients;
    private final Queue<Command> commandEntryQueue;
    private final Queue<Event> eventExitQueue;

    public RmiServer() throws Exception {
        this.clients = new ArrayList<>();
        this.commandEntryQueue = new LinkedList<Command>();
        this.eventExitQueue = new LinkedList<Event>();
    }

    public void run() {
        System.out.println("rmi server running");
        processCommandThread();
        processEventThread();
    }

    public void setController(ControllerState controller) {
        this.controller = controller;
    }

    @Override
    public void receiveCommand(Command command) throws IllegalStateException {
        System.out.println("server command queue: " + System.identityHashCode(this.commandEntryQueue));
        synchronized (this) {
            commandEntryQueue.add(command);
            notifyAll();
        }
    }

    public void processCommandThread() {
        new Thread(this::processCommand).start();
    }

    public void processCommand() {
        while (true) {
            try {
                Command command = null;
                synchronized (this) {
                    while (this.commandEntryQueue.isEmpty()) {
                        System.out.println("waiting");
                        this.wait();
                        System.out.println(
                                "server thread command queue: " + System.identityHashCode(this.commandEntryQueue));
                        System.out.println("woken up");
                    }
                    command = this.commandEntryQueue.poll();
                }
                command.execute(controller);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public synchronized void sendEvent(Event event) throws IllegalStateException {
        System.out.println("got event");
        eventExitQueue.add(event);
        notifyAll();
    }

    public void processEventThread() {
        new Thread(this::processEvent).start();
    }

    public void processEvent() {
        // TODO: gestione eccezioni
        while (true) {
            try {
                Event event = null;
                synchronized (this) {
                    while (this.eventExitQueue.isEmpty()) {
                        this.wait();
                    }
                    event = this.eventExitQueue.poll();
                }
                synchronized (this.clients) {
                    for (var client : this.clients) {
                        client.receiveEvent(event);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public void connect(VirtualClient client) {
        System.out.println("client connected");
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }
}