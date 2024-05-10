package it.polimi.ingsw.codexnaturalis.model.game;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import it.polimi.ingsw.codexnaturalis.network.VirtualClient;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import javafx.util.Pair;

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
    private Player nextPlayer;
    private Boolean lastTurn = false;
    private Integer turnCounter = 0;
    final List<VirtualClient> clients = new ArrayList<>();
    private final Map<Player, List<Pair<Strategy, Card>>> strategyMap;
    // private Chat chat;

    public Game(int gameId, RmiServer rmiServer, SocketServer socketServer) {
        this.gameId = gameId;
        this.gameState = new InitState(this, rmiServer, socketServer);
        this.chat = new Chat();
        this.players = new ArrayList<>();
        this.playerHand = new ArrayList<>();
        this.playerStructure = new ArrayList<>();
        this.strategyMap = new HashMap<>();
        // this.chat = new Chat;
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Chat getChat() {
        return this.chat;
    }

    public Player getNextPlayer() {
        return nextPlayer;
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

    public Boolean isLastTurn() {
        return lastTurn;
    }

    public void removeTurn() {
        this.turnCounter--;
    }

    public Integer getTurnCounter() {
        return turnCounter;
    }

    public Map<Player, List<Pair<Strategy, Card>>> getStrategyMap() {
        return strategyMap;
    }
    // public Chat getChat(){return this.chat};

    public void setLastTurn() {
        this.lastTurn = true;
        this.turnCounter = players.size() + players.size() - (players.indexOf(currentPlayer) + 1);
    }

    public void setState(ControllerState state) {
        this.gameState = state;
    }

    public void setNumPlayers(int numPlayers) {
        // FIXME: do this in initState
        for (int i = 0; i < numPlayers - 1; i++) {
            this.players.add(new Player(""));
        }
        this.numPlayers = numPlayers;
    }

    public void setNumParticipants(int numParticipants) {
        this.numParticipants = numParticipants;
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

    // FIXME: quick fix for now
    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
        // currentPlayer = players.get((players.indexOf(currentPlayer) + 1) %
        // players.size());
    }

    public void addParticipant() {
        this.numParticipants++;
    }

    public int getPatternsTotemPoints(Player player, Map<Player, List<Pair<Strategy, Card>>> strategyMap)
            throws IllegalCommandException {
        int points = 0;

        for (Pair<Strategy, Card> pair : strategyMap.get(player)) {
            // TODO: find a better way than compute twice the test also fails if I delete
            // the comment
            // Computes the number of objective satisfied by a player
            // if (pair.getKey().compute(getStructureByPlayer(player), pair.getValue()) > 0)
            // {
            // getStructureByPlayer(player).increaseSatisfiedPatterns();
            // }

            // Points will contain the times an objective is satisfied * the number of
            // points linked to that objective
            points += pair.getKey().compute(getStructureByPlayer(player), pair.getValue());
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
}
