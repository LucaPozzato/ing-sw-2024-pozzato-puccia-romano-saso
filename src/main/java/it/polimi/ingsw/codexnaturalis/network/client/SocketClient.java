package it.polimi.ingsw.codexnaturalis.network.client;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.utils.DefaultValue;
import it.polimi.ingsw.codexnaturalis.view.View;

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


    public SocketClient( Socket socket, boolean isCli) throws RemoteException {
        this.socket = socket;
        this.isCli = isCli;
        this.eventEntryQueue = new LinkedList<Event>();
        this.commandExitQueue = new LinkedList<Command>();
    }

    @Override
    public void run() {

        try {
            this.input = new ObjectInputStream(socket.getInputStream());
            this.output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            } catch (IOException | ClassNotFoundException e) {
                try {
                    input.close();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }

    @Override
    public void receiveEvent(Event event) throws IllegalStateException {
        eventEntryQueue.add(event);
        // notify del thread;
        // manage exception?
    }

    public void processEventThread() {
        new Thread(this::processEvent).start();
    }

    public void processEvent() {
        // TODO: gestione eccezioni
        while (true) {
            try {
                Event event = this.eventEntryQueue.poll();
                synchronized (this.miniModel) {
                    event.doJob(miniModel);
                }
            } catch (Exception e) {
                // System.err.println("");
                break;
            }
        }
    }


    public void sendCommand (Command command) throws IllegalStateException {
        commandExitQueue.add(command);
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
                Command command = this.commandExitQueue.poll();
                try {
                    output.writeObject(command);
                    output.flush();
                    output.reset();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                // System.err.println("");
                break;
            }
        }
    }

    private void runCli() throws RemoteException {
        // this.view = new GameCli();
        // [...]
    }

    private void runGui() throws RemoteException {
        // this.view = new GameGui();
        // [...]
    }
}
