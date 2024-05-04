package it.polimi.ingsw.codexnaturalis.network.server;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class RmiServer implements VirtualServer, Serializable {
    @Serial
    private static final long serialVersionUID = 5284066693164064871L;
    final ControllerState controller;
    final List<VirtualClient> clients;
    private final Queue<Command> commandEntryQueue;
    private final Queue<Event> eventExitQueue;


    public RmiServer(ControllerState controller) {
        this.controller = controller;
        this.clients = new ArrayList<>();
        this.commandEntryQueue = new LinkedList<Command>();
        this.eventExitQueue = new LinkedList<Event>();

    }

    @Override
    public void receiveCommand (Command command) throws IllegalStateException {
        commandEntryQueue.add(command);
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
    public void connect(VirtualClient client) /*throws RemoteException*/ {
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }

}