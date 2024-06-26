package it.polimi.ingsw.codexnaturalis.model.game;

import java.io.Serial;
import java.io.Serializable;
import java.util.*;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.controller.InitState;
import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.Strategy;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import javafx.util.Pair;

/**
 * Represents a game session of CodexNaturalis, collecting and managing all the
 * components and all the necessaries pieces of information related to the
 * current state of the game
 */

public class Game implements Serializable {
    @Serial
    private static final long serialVersionUID = 621094738562930L;
    private int gameId;
    private Chat chat;
    private ControllerState gameState;
    private List<Player> players;
    private List<Hand> playerHand;
    private List<Structure> playerStructure;
    private Deck deck;
    private Board board;
    private int numPlayers;
    private int numParticipants;
    private Player currentPlayer;
    private Boolean lastTurn = false;
    private Integer turnCounter = 0;
    private final Map<Player, String> fromPlayerToId;
    private final Map<Player, List<Pair<Strategy, Card>>> strategyMap;
    private Map<Player, Boolean> connected;
    private Structure backUpStructure;
    private Hand backUpHand;
    private int backUpPoints;
    private boolean skip;
    public final Object controllerLock;
    public Stack<String> eventTracker;

    /**
     * Constructs a new game instance.
     *
     * @param gameId       the unique identifier for the game
     * @param rmiServer    the RMI server instance
     * @param socketServer the socket server instance
     */
    public Game(int gameId, RmiServer rmiServer, SocketServer socketServer) {
        this.gameId = gameId;
        this.gameState = new InitState(this, rmiServer, socketServer);
        this.chat = new Chat();
        this.players = new ArrayList<>();
        this.playerHand = new ArrayList<>();
        this.playerStructure = new ArrayList<>();
        this.strategyMap = new HashMap<>();
        this.fromPlayerToId = new HashMap<>();
        this.connected = new HashMap<>();
        this.backUpStructure = null;
        this.backUpHand = null;
        this.backUpPoints = 0;
        this.skip = false;
        this.controllerLock = new Object();
        this.eventTracker = new Stack<>();
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to add
     */

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Chat getChat() {
        return this.chat;
    }

    public ControllerState getState() {
        return gameState;
    }

    public Deck getDeck() {
        return deck;
    }

    public Board getBoard() {
        return board;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public int getNumParticipants() {
        return numParticipants;
    }

    public int getGameId() {
        return gameId;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Hand getHandByPlayer(Player player) {
        return playerHand.get(players.indexOf(player));
    }

    public List<Structure> getStructures() {
        return playerStructure;
    }

    public List<Hand> getHands() {
        return playerHand;
    }

    public Structure getStructureByPlayer(Player player) {
        return playerStructure.get(players.indexOf(player));
    }

    /**
     * Checks if it is the last turn of the game.
     *
     * @return true if it is the last turn, false otherwise
     */
    public Boolean isLastTurn() {
        return lastTurn;
    }

    /**
     * Decrements the turn counter by one.
     */
    public void removeTurn() {
        this.turnCounter--;
    }

    public Integer getTurnCounter() {
        return turnCounter;
    }

    public Map<Player, List<Pair<Strategy, Card>>> getStrategyMap() {
        return strategyMap;
    }

    public Map<Player, String> getFromPlayerToId() {
        return this.fromPlayerToId;
    }

    public Map<Player, Boolean> getConnected() {
        return this.connected;
    }

    public boolean getSkip() {
        return this.skip;
    }

    public void setLastTurn() {
        this.lastTurn = true;
        this.turnCounter = players.size() + players.size() - (players.indexOf(currentPlayer) + 1);
    }

    public void setState(ControllerState state) {
        this.gameState = state;
    }

    /**
     * Sets the number of players in the game and populates the list of players
     *
     * @param numPlayers the number of players
     */
    public void setNumPlayers(int numPlayers) {
        for (int i = 0; i < numPlayers - 1; i++) {
            Player newPlayer = new Player("", "");
            this.players.add(newPlayer);
            this.connected.put(newPlayer, true);
            System.out.println("connected size = " + this.connected.size());
        }
        this.numPlayers = numPlayers;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setPlayerHand(Player player, Hand hand) {
        playerHand.add(players.indexOf(player), hand);
    }

    public void setPlayerStructure(Player player, Structure structure) {
        playerStructure.add(players.indexOf(player), structure);
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public void setBackUpStructure(Structure backUpStructure) {
        System.out.println("setting backup structure");
        this.backUpStructure = backUpStructure;
    }

    public void setBackUpHand(Hand backUpHand) {
        System.out.println("setting backup hand");
        this.backUpHand = backUpHand;
    }

    public void setBackUpPoints(int points) {
        System.out.println("setting backup points");
        this.backUpPoints = points;
    }

    public void setSkip(boolean skip) {
        this.skip = skip;
    }

    public void addParticipant() {
        this.numParticipants++;
    }

    /**
     * Computes the total points for the patterns and totems for a player.
     *
     * @param player      the player
     * @param strategyMap the strategy map
     * @return the total points
     * @throws IllegalCommandException if an illegal command occurs
     */
    public int getPatternsTotemPoints(Player player, Map<Player, List<Pair<Strategy, Card>>> strategyMap)
            throws IllegalCommandException {
        int points = 0;

        for (Pair<Strategy, Card> pair : strategyMap.get(player)) {

            // Points will contain the times an objective is satisfied * the number of
            // points linked to that objective
            int oldPoints = points;
            points += pair.getKey().compute(getStructureByPlayer(player), pair.getValue());

            // Computes the number of objective satisfied by a player
            if(points > oldPoints){
                getStructureByPlayer(player).setSatisfiedObj();
            }

            // Clears the set visited attribute in the map and prepares for another search
            for (Card card : getStructureByPlayer(player).getCardToCoordinate().keySet()) {
                getStructureByPlayer(player).getCardToCoordinate().get(card).setVisited(false);
            }
        }

        return points;
    }

    public void throwException(String message) {
        // update client view with message
        // client.updateError(message);
    }

    /**
     * Gets a player by their clientId.
     *
     * @param clientId the client ID
     * @return the player, or null if not found
     */
    public Player PlayerFromId(String clientId) {
        for (var player : fromPlayerToId.keySet()) {
            if (clientId.equals(fromPlayerToId.get(player))) {
                return player;
            }
        }
        return null;
    }

    /**
     * Checks if only one connected player is left in the game.
     *
     * @return true if only one player is left, false otherwise
     */
    public boolean onePlayerLeft() {
        int counter = 0;
        System.out.println("counting players: " + counter);
        for (var player : connected.keySet()) {
            if (connected.get(player)) {
                counter++;
                System.out.println("counting players: " + counter);
                if (counter > 1)
                    return false;
            }
        }
        return true;
    }

    /**
     * Reverts the state of the player's structure and hand in case a disconnection
     * occurs
     * just after a card placement but before drawing a new card.
     */
    public void revert() {
        System.out.println("restoring " + playerStructure.get(players.indexOf(currentPlayer)));
        playerStructure.set(players.indexOf(currentPlayer), backUpStructure);
        System.out.println("restored " + playerStructure.get(players.indexOf(currentPlayer)));
        playerHand.set(players.indexOf(currentPlayer), backUpHand);
        try {
            this.board.updateActualScore(currentPlayer, -backUpPoints);
        } catch (IllegalCommandException e) {
            e.printStackTrace();
        }
    }

    public void pushEvent(String eventMessage) {
        this.eventTracker.push(eventMessage);
    }

    public Stack<String> getEventTracker() {
        return this.eventTracker;
    }
}
