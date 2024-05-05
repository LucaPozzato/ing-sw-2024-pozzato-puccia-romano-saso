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
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class SocketServer implements VirtualServer, Runnable {
    private ControllerState controller;
    final List<VirtualClient> clients;
    private final Queue<Command> commandEntryQueue;
    private final Queue<Event> eventExitQueue;

    public SocketServer() throws IOException {
        this.clients = new ArrayList<>();
        this.commandEntryQueue = new LinkedList<Command>();
        this.eventExitQueue = new LinkedList<Event>();
    }

    public void setController(ControllerState controller) {
        this.controller = controller;
    }

    @Override
    public void receiveCommand(Command command) throws RemoteException {
        commandEntryQueue.add(command);
        notifyAll();
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
                        this.wait();
                    }
                    command = this.commandEntryQueue.poll();
                }
                synchronized (this.controller) {
                    assert command != null;
                    command.execute(controller);
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    @Override
    public synchronized void sendEvent(Event event) throws RemoteException {
        eventExitQueue.add(event);
        notifyAll();
    }

    public void processEventThread() {
        new Thread(this::processEvent).start();
    }

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

        // Socket socket = null;
        // while (!Thread.interrupted()) {
        // socket = this.accept();
        // SocketSkeleton client = new SocketSkeleton(this, socket);
        // clients.add(client);
        // new Thread(client).start();
        // }
        // } catch (IOException e) {
        // throw new RuntimeException(e);
        // }
    }
}