package it.polimi.ingsw.codexnaturalis.network.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Timer;
import java.util.TimerTask;

import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.VirtualServer;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.commands.CreateGameCommand;
import it.polimi.ingsw.codexnaturalis.network.commands.JoinGameCommand;
import it.polimi.ingsw.codexnaturalis.network.commands.Ping;
import it.polimi.ingsw.codexnaturalis.network.events.ErrorEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.InLobbyEvent;
import it.polimi.ingsw.codexnaturalis.network.events.RejoinGameEvent;
import it.polimi.ingsw.codexnaturalis.utils.DefaultValue;

public class SocketServer implements VirtualServer, Runnable {

    private Map<Integer, Game> games;
    private final Map<Integer, List<VirtualClient>> players;
    private final Map<VirtualClient, String> clientIds;
    private final List<VirtualClient> clients;
    private final Map<VirtualClient, Timer> timers;
    private final Queue<Command> commandEntryQueue;
    private final Queue<Event> eventExitQueue;
    private RmiServer rmiServer;

    public SocketServer() throws IOException {
        this.clients = new ArrayList<>();
        this.clientIds = new HashMap<>();
        this.timers = new HashMap<>();
        this.commandEntryQueue = new LinkedList<Command>();
        this.eventExitQueue = new LinkedList<Event>();
        this.players = new HashMap<>();
    }

    public void setGames(Map<Integer, Game> games) {
        this.games = games;
    }

    public void setRmiServer(RmiServer rmiServer) {
        this.rmiServer = rmiServer;
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
                        this.wait();
                    }
                    command = this.commandEntryQueue.poll();
                }
                Integer gameId = command.getGameId();

                if (!(command instanceof Ping)) {
                    System.out.println("socket server command received: " + command.getClass().getName());
                }

                VirtualClient client = null;
                for (var c : clients) {
                    if (c.getClientId().equals(command.getClientId())) {
                        client = c;
                        break;
                    }
                }

                if (command instanceof CreateGameCommand) {
                    if (!games.containsKey(gameId)) {
                        System.out.println("socket server creating a new game");
                        games.put(gameId, new Game(gameId, rmiServer, this));
                    } else {
                        client.receiveEvent(
                                new ErrorEvent(command.getClientId(), command.getGameId(), "gameId already taken"));
                    }
                }

                if ((command instanceof JoinGameCommand || command instanceof CreateGameCommand) && isAlreadyInGame(
                        command.getClientId())/* players.get(command.getGameId()).contains(client) */) {
                    this.sendEvent(new ErrorEvent(command.getClientId(), command.getGameId(),
                            "Already created or joined a game"));
                } else if (command instanceof Ping) {

                    if (!clientIds.containsKey(client))
                        clientIds.put(client, command.getClientId());

                    if (timers.keySet().contains(client)) {
                        timers.get(client).cancel();
                        timers.put(client, new Timer());
                        timers.get(client).schedule(new PingTask(client), 6000);
                    } else
                        try {
                            this.timers.put(client, new Timer());
                            timers.get(client).schedule(new PingTask(client), 6000);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                } else if (games.containsKey(gameId))
                    synchronized (games.get(gameId).controllerLock) {
                        command.execute(games.get(gameId).getState());
                    }
                else
                    this.sendEvent(new ErrorEvent(command.getClientId(), command.getGameId(), "gameId not valid"));

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
     * gets the lock on the server and if the queue is Empty waits for an event to
     * be added
     * once awoken, removes the event from the queue, synchronizes on the list of
     * client skeletons and
     * for each client skeleton it calls the method exposed by the client skeleton
     * receiveEvent(), passing the event
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
                    String[] eventName = event.getClass().getName().split("\\.");
                    System.out.println("> " + eventName[eventName.length - 1] + " processed");

                }

                Integer gameId = event.getGameId();
                VirtualClient client = null;

                if (event instanceof InLobbyEvent || event instanceof RejoinGameEvent) {
                    if (!players.containsKey(gameId))
                        players.put(gameId, new ArrayList<>());
                    for (var c : clients) {
                        if (c.getClientId() != null && c.getClientId().equals(event.getClientId())) {
                            client = c;
                            players.get(gameId).add(client);
                            break;
                        }
                    }
                } else if (event instanceof ErrorEvent) {
                    for (var c : clients)
                        if (c.getClientId() != null && c.getClientId().equals(event.getClientId())) {
                            client = (SocketSkeleton) c;
                            if (players.get(gameId) == null || !players.get(gameId).contains(client))
                                c.receiveEvent(event);
                            System.out.println("> error event sent to client");
                            // if (!players.get(gameId).contains(client))
                            // players.get(gameId).add(client);
                            break;
                        }
                }

                if (this.players.get(gameId) != null) {
                    synchronized (this.players.get(gameId)) {
                        for (var c : this.players.get(gameId)) {
                            System.out.println(c);
                            c.receiveEvent(event);
                            System.out.println("> event sent to client");
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    // nevers used [yet it could be used to clean the code]
    @Override
    public void connect(VirtualClient client) {
    }

    /**
     * starts the server by initializing processing threads for commands and events,
     * establishes a socket connection to accept incoming client connections
     * each incoming connection is handled by creating a new socket client instance
     * and starting it in a separate thread.
     * If an IOException occurs while accepting a new connection, the stack trace
     * is printed, and the server continues to run.
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
                System.out.println("clients: " + clients.size());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Task class to handle periodic ping tasks for clients
     */
    class PingTask extends TimerTask {
        private final VirtualClient client;

        /**
         * Constructs a new PingTask for the specified client
         * 
         * @param client
         */
        public PingTask(VirtualClient client) {
            this.client = client;
        }

        /**
         * executes the ping task to disconnect clients by
         * finding the game in which the client is playing
         * calling the disconnect method from the controller of that game, passing the
         * clientId
         * removes the client from the list of clients
         * removes the clientId from the list of clientIds
         */
        @Override
        public void run() {

            boolean exit;
            Integer gId = null;

            for (var gameId : players.keySet()) {
                exit = false;
                for (var client1 : players.get(gameId)) {
                    if (client.equals(client1)) {

                        players.get(gameId).remove(client);
                        gId = gameId;
                        exit = true;
                        break;
                    }
                }
                if (exit)
                    break;
            }

            if (gId != null && games.containsKey(gId))
                try {
                    games.get(gId).getState().disconnect(client.getClientId());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            clients.remove(client);
            clientIds.remove(client);
        }

    }

    public boolean isAlreadyInGame(String clientId) {
        for (var gameId : players.keySet()) {
            for (var client1 : players.get(gameId)) {
                try {
                    String cId1 = client1.getClientId();
                    if (clientId.equals(cId1)) {
                        return true;
                    }
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
        return false;
    }
}