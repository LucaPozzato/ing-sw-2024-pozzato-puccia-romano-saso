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
import it.polimi.ingsw.codexnaturalis.network.client.RmiClient;
import it.polimi.ingsw.codexnaturalis.network.commands.Command;
import it.polimi.ingsw.codexnaturalis.network.commands.CreateGameCommand;
import it.polimi.ingsw.codexnaturalis.network.commands.JoinGameCommand;
import it.polimi.ingsw.codexnaturalis.network.events.ErrorEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.InLobbyEvent;

public class RmiServer implements VirtualServer {
    private Map<Integer, Game> games;
    private final Map<Integer, List<VirtualClient>> players;
    private Map<VirtualClient, Boolean> connected;
    private final List<VirtualClient> clients;
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
        this.commandEntryQueue = new LinkedList<>();
        this.eventExitQueue = new LinkedList<>();
        this.players = new HashMap<>();
        this.connected = new HashMap<>();
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

    // public void setModel(Game model) {
    // this.model = model;
    // }

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

                // password setting rmi
                if ((command instanceof CreateGameCommand) || (command instanceof JoinGameCommand)) {
                    for (var c : clients) {
                        if (c.getClientId().equals(command.getClientId())) {
                            c.setPassword(command.getPassword());
                            break;
                        }
                    }
                }

                System.out.println("rmi server received command with gameIdd: " + gameId);
                System.out.println("rmi server command received: " + command.getClass().getName());

                if (command instanceof CreateGameCommand) {
                    if (!games.containsKey(gameId)) {
                        System.out.println("rmi server creating a new game");
                        games.put(gameId, new Game(gameId, this, socketServer));
                    } else {
                        RmiClient client = null;
                        for (var c : clients) {
                            if (c.getClientId().equals(command.getClientId())) {
                                client = (RmiClient) c;
                                break;
                            }
                        }
                        client.receiveEvent(
                                new ErrorEvent(command.getClientId(), command.getGameId(), "gameId already taken"));
                    }
                }

                // needs to verify the gameid, that the nickname exists among the nickname of
                // the clients in that game, tha the client connected to the clientID connected
                // to the nickname in that game is connected
                // than needs to cancel the old change his clinetId with the old Clientid
                // update the list of clinets, the list of connected and the list of players,
                // if (command instanceof ReJoinCommand)
                // if (games.containsKey(gameId))
                // for ( String clId : players.get(gameId) ) {
                // String existingClientId =
                // (games.get(gameId).ClientIdFromNickname(command.getNickName()));
                // //controlliamo che il nickname nel comando sia tra i nickanme dei plaeyrs in
                // partita
                // RmiClient client = null;
                // if (existingClientId.equals(command.getClientId()))
                // for (var cl : clients){
                // if (cl.getClientId = clId)
                //
                // }
                // if (command.getPassword().eq
                // uals(getPassword()))
                //
                //
                // }

                String[] commandName = command.getClass().getName().split("\\.");
                if (games.containsKey(gameId))
                    command.execute(games.get(gameId).getState());
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

                if (event instanceof InLobbyEvent) {
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
                            if (!players.get(gameId).contains(client))
                                players.get(gameId).add(client);
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
    }

    private void pinger() {
        System.out.println("started thread pinger");
        while (true) {
            VirtualClient client = null;
            for (var c : clients) {
                // System.out.println("trying to ping");
                client = c;
                // System.out.println("pinging client");
                try {
                    client.ping();
                    connected.put(client, true);
                } catch (ConnectException e) {
                    connected.put(client, false);

                    System.out.println("client disconnected");
                    Boolean found = false;
                    Integer gId = null;
                    for (var gameId : players.keySet())
                        for (var c1 : players.get(gameId))
                            if (client.equals(c1)) {
                                gId = gameId;
                                found = true;
                                break;
                            }
                    // BUG: maybe it should be if (found)
                    if (!found) {
                        clients.remove(client);
                        connected.remove(client);
                    }
                    // } else
                    // games.get(gId).getState().disconnect(client.getClientId());

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                // System.out.println("client ponged");
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}