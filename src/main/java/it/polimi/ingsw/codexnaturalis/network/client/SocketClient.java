package it.polimi.ingsw.codexnaturalis.network.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.commands.Ping;
import it.polimi.ingsw.codexnaturalis.network.events.EndGameEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.ForcedEndEvent;
import it.polimi.ingsw.codexnaturalis.view.View;
import it.polimi.ingsw.codexnaturalis.view.gui.GuiApp;
import it.polimi.ingsw.codexnaturalis.view.tui.Tui;

public class SocketClient implements VirtualClient, Runnable {
    private final String clientId;
    private final Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private final Queue<Event> eventEntryQueue;
    private final Queue<Command> commandExitQueue;
    private final boolean isCli;
    private View view;
    private final MiniModel miniModel;

    /**
     * costructor of the RmiClient, sets the socket to which the client will
     * connect, the client interface choice and
     * instantiates the MiniModel and the two queues that will manage the events and
     * the commands
     * 
     * @param socket
     * @param isCli
     * @throws RemoteException
     */
    public SocketClient(Socket socket, boolean isCli) throws RemoteException {
        this.clientId = createClientId();
        this.socket = socket;
        this.isCli = isCli;
        this.miniModel = new MiniModel();
        this.eventEntryQueue = new LinkedList<Event>();
        this.commandExitQueue = new LinkedList<Command>();
    }

    /**
     * this method is called right after the creation of the client by the
     * clientMain
     * connects the client to the provided socket opening an output and input stream
     * starts two threads to process events and commands
     * starts the cli or the gui according to the client choice
     * creates and infinite loop which keeps on reading from the input stream
     * connected to the server
     * it then deserializes the information creating an event and calls the
     * receiveEvent method
     */
    @Override
    public void run() {
        System.out.println("Socket client started");
        try {
            this.output = new ObjectOutputStream(socket.getOutputStream());
            this.input = new ObjectInputStream(socket.getInputStream());

            processEventThread();
            processCommandThread();
            pingThread();

            if (isCli)
                runCli();
            else
                runGui();

            while (true) {
                try {
                    Event event = (Event) input.readObject();
                    receiveEvent(event);

                } catch (IOException e) {
                    System.err.println("Server disconnected");
                    System.exit(0);
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

    /**
     * this method is called by the RmiServer to send an event, which is an update
     * in the model
     * it adds the event to a queue in order to return immediately
     * the event will later be processed by another thread
     * 
     * @param event
     * @throws IllegalStateException
     */
    @Override
    public synchronized void receiveEvent(Event event) throws IllegalStateException {
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

                if (event instanceof ForcedEndEvent || event instanceof EndGameEvent) {
                    Thread.sleep(15000);
                    System.exit(0);
                }

            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }

    /**
     * this method is called by the View in order to send a command taken by input
     * to the server
     * it adds the command to a queue in order to return immediately
     * the event will later be processed by another thread
     * 
     * @param command
     * @throws IllegalStateException
     */
    public synchronized void sendCommand(Command command) throws IllegalStateException {
        commandExitQueue.add(command);
        notifyAll();
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
     * once awoken, removes the command from the queue and writes it the output
     * stream
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
                try {
                    output.writeObject(command);
                    output.flush();
                    output.reset();
                } catch (SocketException e) {
                    System.err.println("Server disconnected");
                    System.exit(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * creates a Cli view and runs it
     * 
     * @throws RemoteException
     */
    private void runCli() throws RemoteException {
        this.view = new Tui(miniModel, this);
        miniModel.setView(view);
        view.run();
    }

    /**
     * creates a Gui view and runs it
     * 
     * @throws RemoteException
     */
    private void runGui() throws RemoteException {
        this.view = new GuiApp(this, miniModel);
        miniModel.setView(view);
        view.run();
    }

    /**
     * generates a clientId which will uniquely dinstinuish a client
     */
    private String createClientId() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    /**
     * creates a Thread to start sending pingCommands
     */
    public void pingThread() {
        new Thread(this::ping).start();
    }

    /**
     * creates and sends a pingCommand every two seconds
     */
    @Override
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

}
