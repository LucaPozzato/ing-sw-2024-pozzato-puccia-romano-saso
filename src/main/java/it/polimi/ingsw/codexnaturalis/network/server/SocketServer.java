package it.polimi.ingsw.codexnaturalis.network.server;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.utils.DefaultValue;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SocketServer implements VirtualServer, Runnable {
    private Game model;
    private final List<VirtualClient> clients;
    private final Queue<Command> commandEntryQueue;
    private final Queue<Event> eventExitQueue;

    public SocketServer() throws IOException {
        this.clients = new ArrayList<>();
        this.commandEntryQueue = new LinkedList<Command>();
        this.eventExitQueue = new LinkedList<Event>();
    }

    public void setModel(Game model) {
        this.model = model;
    }

    /**
     * this method is called by the client to send a command taken by input
     * it adds the command to a queue in order to return immediately
     * the command will later be processed by another thread
     * @param command
     * @throws RemoteException
     */
    @Override
    public void receiveCommand(Command command) throws RemoteException {
        synchronized (this) {
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
     * gets the lock on the server and if the queue is Empty waits for a command to be added
     * once awoken, removes the event from the queue and
     * calls the execution method in the event execute passing the controller
     */
    public void processCommand() {
        while (true) {
            try {
                Command command = null;
                synchronized (this) {
                    while (this.commandEntryQueue.isEmpty()) {
                        System.out.println("socket server waiting");
                        this.wait();
                        System.out.println(
                                "socket server thread command queue: " + System.identityHashCode(this.commandEntryQueue));
                        System.out.println("socket server woken up");
                    }
                    command = this.commandEntryQueue.poll();
                }
                command.execute(model.getState());
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * this method is called by the Model to send to the client an event, which is an update on the model
     * it adds the event to a queue in order to return immediately
     * the event will later be processed by another thread
     * @param event
     * @throws RemoteException
     */
    @Override
    public synchronized void sendEvent(Event event) throws RemoteException {
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
     * gets the lock on the server and if the queue is Empty waits for an event to be added
     * once awoken, removes the event from the queue, synchronizes on the list of client skeletons and
     * for each client skeleton it calls the method exposed by the client skeleton receiveEvent(), passing the event
     */
    public void processEvent() {
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

    /**
     * register the client socket in its list
     * @param client
     */
    @Override
    public void connect(VirtualClient client) {
        System.out.println("socket client connected");
        synchronized (this.clients) {
            this.clients.add(client);
        }
    }

    /**
     * TODO
     * @param client
     */
    public void disconnectClient(VirtualClient client) {
        synchronized (clients) {
            if (clients.remove(client)) {
                if (client instanceof SocketSkeleton) {
                    ((SocketSkeleton) client).stop();
                }
            }
        }
    }

    /**
     * TODO
     */
    public void run() {
        processCommandThread();
        processEventThread();

        Socket socket = null;

        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(DefaultValue.port_Socket)) {
                socket = serverSocket.accept();

                System.err.println(
                        "ServerImpl: connection accepted from " + socket.getInetAddress() + ":" +
                                socket.getPort());

            } catch (IOException e) {
                e.printStackTrace();
            }

            assert socket != null;
            SocketSkeleton client;

            try {
                client = new SocketSkeleton(this, socket);
                new Thread(client).start();
                clients.add(client);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}