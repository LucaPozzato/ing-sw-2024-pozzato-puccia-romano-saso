package it.polimi.ingsw.codexnaturalis.network.server;

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

public class RmiServer implements VirtualServer {
    private Map<Integer, Game> games;
    private final Map<Integer, List<VirtualClient>> players;
    private final List<VirtualClient> clients;
    private final Map<VirtualClient, String> clientIds;
    private final Map<String, Timer> timers;
    private final Queue<Command> commandEntryQueue;
    private final Queue<Event> eventExitQueue;
    private SocketServer socketServer;

    public RmiServer() throws Exception {
        this.clients = new ArrayList<>();
        this.clientIds = new HashMap<>();
        this.timers = new HashMap<>();
        this.commandEntryQueue = new LinkedList<>();
        this.eventExitQueue = new LinkedList<>();
        this.players = new HashMap<>();
    }

    /**
     * starts the server by initializing processing threads for commands and events,
     */
    public void run() {
        System.out.println("rmi server running");
        processCommandThread();
        processEventThread();
    }

    public void setGames(Map<Integer, Game> games) {
        this.games = games;
    }

    public void setSocketServer(SocketServer socketServer) {
        this.socketServer = socketServer;
    }

    /**
     * this method is called by the client to send a command taken by input
     * it adds the command to a queue in order to return immediately
     * the command will later be processed by another thread
     * 
     * @param command
     */
    @Override
    public void receiveCommand(Command command) throws IllegalStateException {
        String[] commandName = command.getClass().getName().split("\\.");
        synchronized (this) {
            if (!(command instanceof Ping))
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
                        this.wait();
                    }
                    command = this.commandEntryQueue.poll();
                }

                Integer gameId = command.getGameId();

                if (!(command instanceof Ping))
                    System.out.println("rmi server command received: " + command.getClass().getName());

                VirtualClient client = null;
                for (var c : clients) {
                    if (clientIds.get(c).equals(command.getClientId())) {
                        client = c;
                        break;
                    }
                }

                if (command instanceof CreateGameCommand) {
                    if (!games.containsKey(gameId)) {
                        System.out.println("rmi server creating a new game");
                        games.put(gameId, new Game(gameId, this, socketServer));
                    } else {
                        client.receiveEvent(
                                new ErrorEvent(command.getClientId(), command.getGameId(), "gameId already taken"));
                    }
                }

                if ((command instanceof JoinGameCommand || command instanceof CreateGameCommand) && isAlreadyInGame(
                        command.getClientId())/* players.get(command.getGameId()).contains(client) */) {
                    this.sendEvent(new ErrorEvent(command.getClientId(), command.getGameId(),
                            "Already created or joined a game"));
                } else if (command instanceof Ping && timers.containsKey(command.getClientId())) {
                    timers.get(command.getClientId()).cancel();
                    timers.put(command.getClientId(), new Timer());
                    timers.get(command.getClientId()).schedule(new PingTask(command.getClientId()), 6000);
                } else if (games.containsKey(gameId))
                    synchronized (games.get(gameId).controllerLock) {
                        command.execute(games.get(gameId).getState());
                    }
                else
                    this.sendEvent(new ErrorEvent(command.getClientId(), command.getGameId(), "gameId not valid"));

                String[] commandName = command.getClass().getName().split("\\.");

                if (!(command instanceof Ping))
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
     * @throws IllegalStateException
     */
    @Override
    public synchronized void sendEvent(Event event) throws IllegalStateException {
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
                    for (var c : clients)
                        if (clientIds.get(c) != null && clientIds.get(c).equals(event.getClientId())) {
                            client = c;
                            players.get(gameId).add(client);
                            break;
                        }
                } else if (event instanceof ErrorEvent) {
                    for (var c : clients)
                        if (clientIds.get(c) != null && clientIds.get(c).equals(event.getClientId())) {
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
        synchronized (this.timers) {
            try {
                this.timers.put(client.getClientId(), new Timer());
                timers.get(client.getClientId()).schedule(new PingTask(client.getClientId()), 6000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Task class to handle periodic ping tasks for clients
     */
    class PingTask extends TimerTask {
        private final String clientId;

        /**
         * Constructs a new PingTask for the specified client
         * 
         * @param client
         */
        public PingTask(String clientId) {
            this.clientId = clientId;
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
            VirtualClient client = null;
            boolean exit = false;
            Integer gId = null;

            // troviamo il client
            for (var c : clientIds.keySet()) {
                if (clientIds.get(c).equals(clientId)) {
                    client = c;
                    break;
                }
            }

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

            clients.remove(client);
            clientIds.remove(client);
            if (gId != null && games.containsKey(gId))
                games.get(gId).getState().disconnect(clientId);
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