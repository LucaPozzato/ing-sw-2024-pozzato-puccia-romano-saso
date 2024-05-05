package it.polimi.ingsw.codexnaturalis.network.client;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.view.View;
import it.polimi.ingsw.codexnaturalis.view.tui.Tui;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;

public class RmiClient extends UnicastRemoteObject implements VirtualClient {

    private final VirtualServer server;
    private final Queue<Event> eventEntryQueue;
    private final Queue<Command> commandExitQueue;
    private final boolean isCli;
    private View view;
    private MiniModel miniModel;

    public RmiClient(VirtualServer server, boolean isCli) throws RemoteException {
        this.server = server;
        this.isCli = isCli;
        this.miniModel = new MiniModel();
        this.eventEntryQueue = new LinkedList<Event>();
        this.commandExitQueue = new LinkedList<Command>();
    }

    public void run() throws RemoteException {
        server.connect(this);

        if (isCli)
            runCli();
        else
            runGui();
    }

    @Override
    public void receiveEvent(Event event) throws RemoteException {
        eventEntryQueue.add(event);
        notifyAll();
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

    public void sendCommand(Command command) throws RemoteException {
        commandExitQueue.add(command);
        notifyAll();;
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
                synchronized (this.server) {
                    assert command != null;
                    this.server.receiveCommand(command);
                }
            } catch (Exception e) {
                // System.err.println("");
                break;
            }
        }
    }

    private void runCli() throws RemoteException {
        this.view = new Tui(miniModel, this);
        miniModel.setView(view);
        view.run();
        // [...]
    }

    private void runGui() throws RemoteException {
        // this.view = new GameGui();
        // [...]
    }

}
