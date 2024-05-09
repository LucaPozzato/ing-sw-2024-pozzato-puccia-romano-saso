package it.polimi.ingsw.codexnaturalis.network.server;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;

public class RmiServer implements VirtualServer {
    //private Map<Integer,Game> games;
    private Game model;
    private final List<VirtualClient> clients;
    private final Queue<Command> commandEntryQueue;
    private final Queue<Event> eventExitQueue;

    /**
     * constructor of the RmiServer, it instantiates the list of the clients
     * connected and
     * the queues that will manage the events and the commands
     * 
     * @throws Exception
     */
    public RmiServer() throws Exception {
        this.clients = new ArrayList<>();
        this.commandEntryQueue = new LinkedList<>();
        this.eventExitQueue = new LinkedList<>();
        //this.games = new HashMap<>();
    }

    /**
     * starts two threads to process events and commands
     */
    public void run() {
        System.out.println("rmi server running");
        processCommandThread();
        processEventThread();
    }

    public void setModel(Game model) {
        this.model = model;
    }

    /**
     * this method is called by the client to send a command taken by input
     * it adds the command to a queue in order to return immediately
     * the command will later be processed by another thread
     * 
     * @param command
     * @throws RemoteException
     */
    @Override
    public void receiveCommand(Command command) throws IllegalStateException {
        String[] commandName = command.getClass().getName().split("\\.");
        synchronized (this) {
            System.out.println("> " + commandName[commandName.length - 1] + " received");
            commandEntryQueue.add(command);
            notifyAll();
        }
    }

    /**
     * creates a Thread to process the commands received
     */
    public void processCommandThread() {
        new Thread(this::processCommand).start();
    }

    /**
     * this method creates an infinite loop in which it
     * gets the lock on the server and if the queue is Empty waits for a command to
     * be added
     * once awoken, removes the event from the queue and
     * calls the execution method in the event execute passing the controller
     */
    public void processCommand() {
        while (true) {
            try {
                Command command = null;
                synchronized (this) {
                    while (this.commandEntryQueue.isEmpty()) {
                        System.out.println("server command entry queue thread waiting");
                        this.wait();
                        System.out.println("server command entry queue thread woken up");
                    }
                    command = this.commandEntryQueue.poll();
                }
                String[] commandName = command.getClass().getName().split("\\.");
                command.execute(model.getState());
                System.out.println("> " + commandName[commandName.length - 1] + " executed");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * this method is called by the Model to send to the client an event, which is
     * an update on the model
     * it adds the event to a queue in order to return immediately
     * the event will later be processed by another thread
     * 
     * @param event
     * @throws RemoteException
     */
    @Override
    public synchronized void sendEvent(Event event) throws IllegalStateException {
        String[] eventName = event.getClass().getName().split("\\.");
        System.out.println("> " + eventName[eventName.length - 1] + " added to queue");
        eventExitQueue.add(event);
        notifyAll();
    }

    /**
     * creates a Thread to process the events to send
     */
    public void processEventThread() {
        new Thread(this::processEvent).start();
    }

    /**
     * this method creates an infinite loop that
     * gets the lock on the server and if the queue is Empty waits for an event to
     * be added
     * once awoken, removes the event from the queue, synchronizes on the list of
     * clients and
     * for each client it calls the method exposed by the client receiveEvent()
     * passing the event
     */
    public void processEvent() {
        // TODO: gestione eccezioni
        while (true) {
            try {
                Event event = null;
                synchronized (this) {
                    while (this.eventExitQueue.isEmpty()) {
                        System.out.println("server event exit queue thread waiting");
                        this.wait();
                        System.out.println("server event exit queue thread woken up");
                    }
                    event = this.eventExitQueue.poll();
                    String[] eventName = event.getClass().getName().split("\\.");
                    System.out.println("> " + eventName[eventName.length - 1] + " processed");
                }
                synchronized (this.clients) {
                    for (var client : this.clients) {
                        client.receiveEvent(event);
                        System.out.println("> event sent to client");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * register the client in its list
     * 
     * @param client
     */
    @Override
    public void connect(VirtualClient client) {
        System.out.println("client connected");
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }
}