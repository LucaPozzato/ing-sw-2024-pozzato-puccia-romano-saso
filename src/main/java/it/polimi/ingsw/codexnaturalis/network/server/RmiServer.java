package it.polimi.ingsw.codexnaturalis.network.server;

import java.rmi.ConnectException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.commands.CreateGameCommand;
import it.polimi.ingsw.codexnaturalis.network.events.ErrorEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.InLobbyEvent;
import it.polimi.ingsw.codexnaturalis.network.events.RejoinGameEvent;

public class RmiServer implements VirtualServer {
    private Map<Integer, Game> games;
    private final Map<Integer, List<VirtualClient>> players;
    private final List<VirtualClient> clients;
    private final Map<VirtualClient, String> clientIds;
    private final Queue<Command> commandEntryQueue;
    private final Queue<Event> eventExitQueue;
    private SocketServer socketServer;

    /**
     * constructor of the RmiServer, it instantiates the list of the clients
     * connected and
     * the queues that will manage the events and the commands
     * 
     * @throws Exception
     */
    public RmiServer() throws Exception {
        this.clients = new ArrayList<>();
        this.clientIds = new HashMap<>();
        this.commandEntryQueue = new LinkedList<>();
        this.eventExitQueue = new LinkedList<>();
        this.players = new HashMap<>();
    }

    /**
     * starts two threads to process events and commands
     */
    public void run() {
        System.out.println("rmi server running");
        processCommandThread();
        processEventThread();
        new Thread(this::pinger).start();
    }

    public void setGames(Map<Integer, Game> games) {
        this.games = games;
    }

    public void setSocketServer(SocketServer socketServer) {
        this.socketServer = socketServer;
    }

    // public Map<SocketSkeleton, Boolean> getConnected() {
    // return connected;
    // }

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
                        System.out.println("rmi server command entry queue thread waiting");
                        this.wait();
                        System.out.println("rmi server command entry queue thread woken up");
                    }
                    command = this.commandEntryQueue.poll();
                }

                Integer gameId = command.getGameId();

                // TODO: eliminare password dal client e tutto il settaggio

                System.out.println("rmi server received command with gameIdd: " + gameId);
                System.out.println("rmi server command received: " + command.getClass().getName());

                if (command instanceof CreateGameCommand) {
                    if (!games.containsKey(gameId)) {
                        System.out.println("rmi server creating a new game");
                        games.put(gameId, new Game(gameId, this, socketServer));
                    } else {
                        VirtualClient client = null;
                        for (var c : clients) {
                            if (clientIds.get(c).equals(command.getClientId())) {
                                client = c;
                                break;
                            }
                        }
                        client.receiveEvent(
                                new ErrorEvent(command.getClientId(), command.getGameId(), "gameId already taken"));
                    }
                }

                String[] commandName = command.getClass().getName().split("\\.");
                if (games.containsKey(gameId))
                    synchronized (games.get(gameId).controllerLock) {
                        command.execute(games.get(gameId).getState());
                    }
                else
                    this.sendEvent(new ErrorEvent(command.getClientId(), command.getGameId(), "gameId not valid"));
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

                Integer gameId = event.getGameId();
                VirtualClient client = null;

                if (event instanceof InLobbyEvent || event instanceof RejoinGameEvent) {
                    if (!players.containsKey(gameId))
                        players.put(gameId, new ArrayList<>());
                    for (var c : clients)
                        if (c.getClientId() != null && c.getClientId().equals(event.getClientId())) {
                            client = c;
                            players.get(gameId).add(client);
                            break;
                        }
                } else if (event instanceof ErrorEvent) {
                    for (var c : clients)
                        if (c.getClientId() != null && c.getClientId().equals(event.getClientId())) {
                            client = c;
                            // fix
                            if (players.get(gameId) == null || !players.get(gameId).contains(client))
                                c.receiveEvent(event);
                            System.out.println("> setting error event sent to client");
                            // if (!players.get(gameId).contains(client))
                            // players.get(gameId).add(client);
                            break;
                        }
                }

                if (this.players.get(gameId) != null) {
                    synchronized (this.players.get(gameId)) {
                        for (var c : this.players.get(gameId)) {
                            c.receiveEvent(event);
                            System.out.println(c);
                            System.out.println("> event sent to client");
                        }
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
        synchronized (this.clientIds) {
            try {
                this.clientIds.put(client, client.getClientId());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    private void pinger() {
        System.out.println("started thread pinger");
        VirtualClient c;
        boolean disconnected;
        boolean exit;
        Integer gId;
        String cId;

        while (true) {

            c = null;
            disconnected = false;
            gId = null;
            cId = null;

            for (var client : clients) {
                // System.out.println("pinging client");
                try {
                    client.ping();
                    System.out.println("pinging clienttt ");
                } catch (ConnectException e) {

                    System.out.println("client disconnected");

                    for (var gameId : players.keySet()) {
                        exit = false;
                        for (var client1 : players.get(gameId))
                            if (client.equals(client1)) {

                                players.get(gameId).remove(client);
                                gId = gameId;
                                exit = true;
                                break;
                            }

                        if (exit)
                            break;
                    }

                    c = client;
                    cId = clientIds.get(c);
                    disconnected = true;
                    break;

                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                // System.out.println("client ponged");
            }

            if (disconnected) {
                System.out.println("removing client");
                clients.remove(c);
                clientIds.remove(c);
                System.out.println("disconnected client found");
                System.out.println("trying to disconnect client");
                games.get(gId).getState().disconnect(cId);
            }

            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}