package it.polimi.ingsw.codexnaturalis.network.client;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.view.View;
import it.polimi.ingsw.codexnaturalis.view.tui.Tui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;

public class SocketClient implements VirtualClient, Runnable {
    private final Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final Queue<Event> eventEntryQueue;
    private final Queue<Command> commandExitQueue;
    private final boolean isCli;
    private View view;
    private MiniModel miniModel;

    public SocketClient(Socket socket, boolean isCli) throws RemoteException {
        this.socket = socket;
        this.isCli = isCli;
        this.miniModel = new MiniModel();
        this.eventEntryQueue = new LinkedList<Event>();
        this.commandExitQueue = new LinkedList<Command>();
    }

    @Override
    public void run() {
        System.out.println("Socket client started");
        try {
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());

            if (isCli) {
                try {
                    runCli();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    runGui();
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            while (true) {
                try {
                    Event event = (Event) input.readObject();
                    receiveEvent(event);
                } catch (IOException e) {
                    System.err.println("Error while reading event from socket: " + e.getMessage());
                    break;
                }
            }

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while running socket client: " + e.getMessage());
        } finally {
            try {
                input.close();
                output.close();
                socket.close();
            } catch (IOException e) {
                System.err.println("Error while closing resources: " + e.getMessage());
            }
        }
    }

    @Override
    public synchronized void receiveEvent(Event event) throws IllegalStateException {
        eventEntryQueue.add(event);
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
                    while (this.eventEntryQueue.isEmpty()) {
                        this.wait();
                    }
                    event = this.eventEntryQueue.poll();
                }
                synchronized (this.miniModel) {
                    event.doJob(miniModel);
                }

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public synchronized void sendCommand(Command command) throws IllegalStateException {
        commandExitQueue.add(command);
        notifyAll();
    }

    public void processCommandThread() {
        new Thread(this::processCommand).start();
    }

    public void processCommand() {
        while (true) {
            try {
                Command command = null;
                System.out.println("client thread queue: " + System.identityHashCode(this.commandExitQueue));
                synchronized (this) {
                    while (this.commandExitQueue.isEmpty()) {
                        this.wait();
                    }
                    command = this.commandExitQueue.poll();
                }
                try {
                    output.writeObject(command);
                    output.flush();
                    output.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    private void runCli() throws RemoteException {
        this.view = new Tui(miniModel, this);
        miniModel.setView(view);
        view.run();
    }

    private void runGui() throws RemoteException {
        // this.view = new GameGui();
        // [...]
    }
}
