package it.polimi.ingsw.codexnaturalis.network.client;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.commands.Ping;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.view.View;
import it.polimi.ingsw.codexnaturalis.view.gui.GuiApp;
import it.polimi.ingsw.codexnaturalis.view.tui.Tui;

public class RmiClient extends UnicastRemoteObject implements VirtualClient {
    private final String clientId;
    private final VirtualServer server;
    private final Queue<Event> eventEntryQueue;
    private final Queue<Command> commandExitQueue;
    private final boolean isCli;
    private View view;
    private final MiniModel miniModel;

    /**
     * costructor of the RmiClient, sets the server to which the client connect, the
     * client interface choice and
     * instantiates the MiniModel and the two queues that will manage the events and
     * the commands
     * 
     * @param server
     * @param isCli
     * @throws RemoteException
     */
    public RmiClient(VirtualServer server, boolean isCli) throws RemoteException {
        this.clientId = createClientId();
        this.server = server;
        this.isCli = isCli;
        this.miniModel = new MiniModel();
        this.eventEntryQueue = new LinkedList<Event>();
        this.commandExitQueue = new LinkedList<Command>();
    }

    /**
     * this method is called right after the creation of the client by the
     * clientMain
     * it connects the client to the server
     * starts two threads to process events and commands
     * starts the cli or the gui according to the client choice
     * 
     * @throws RemoteException
     */
    public void run() throws RemoteException {
        this.server.connect(this);
        processEventThread();
        processCommandThread();
        pingThread();

        if (isCli)
            runCli();
        else
            runGui();
    }

    /**
     * this method is called by the RmiServer to send an event, which is an update
     * in the model.
     * it adds the event to a queue in order to return immediately
     * the event will later be processed by another thread
     * 
     * @param event
     * @throws RemoteException
     */
    @Override
    public synchronized void receiveEvent(Event event) throws RemoteException {
        if (event.getClientId() == null) {
            eventEntryQueue.add(event);
            notifyAll();
        } else {
            if (clientId.equals(event.getClientId())) {
                eventEntryQueue.add(event);
                notifyAll();
            }
        }
    }

    /**
     * creates a Thread to process the events received
     */
    public void processEventThread() {
        new Thread(this::processEvent).start();
    }

    /**
     * this method creates an infinite loop in which it
     * gets the lock on the client and if the queue is Empty waits for an event to
     * be added
     * once awoken, removes the event from the queue, synchronizes on the MiniModel
     * and
     * calls the execution method in the event doJob passing the MiniModel
     */
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
            }
        }
    }

    /**
     * this method is called by the [view?] to send to the server a command taken by
     * input.
     * it adds the command to a queue in order to return immediately
     * the event will later be processed by another thread
     * 
     * @param command
     * @throws RemoteException
     */
    public void sendCommand(Command command) throws RemoteException {
        synchronized (this) {
            commandExitQueue.add(command);
            notifyAll();
        }
    }

    /**
     * creates a Thread to process the commands to send
     */
    public void processCommandThread() {
        new Thread(this::processCommand).start();
    }

    /**
     * this method creates an infinite loop that
     * gets the lock on the client and if the queue is Empty waits for a command to
     * be added
     * once awoken, removes the command from the queue, synchronizes on the server
     * and
     * calls the method exposed by the server receiveCommand() passing the command
     */
    public void processCommand() {
        while (true) {
            try {
                Command command = null;
                synchronized (this) {
                    while (this.commandExitQueue.isEmpty()) {
                        this.wait();
                    }
                    command = this.commandExitQueue.poll();
                }
                this.server.receiveCommand(command);

            } catch (UnmarshalException | ConnectException ex) {
                System.out.println("Server disconnected");
                System.exit(0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * this creates a Cli view and runs it
     * 
     * @throws RemoteException
     */
    private void runCli() throws RemoteException {
        this.view = new Tui(miniModel, this);
        miniModel.setView(view);
        view.run();
    }

    public void pingThread() {
        new Thread(this::ping).start();
    }

    public void ping() {
        while (true) {
            try {
                sendCommand(new Ping(clientId));
                Thread.sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * creates a Gui view and runs it
     * 
     * @throws RemoteException
     */
    private void runGui() throws RemoteException {
        this.view = new GuiApp(this, miniModel);
        miniModel.setView(view); // ?
        view.run();
    }

    private String createClientId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

}
