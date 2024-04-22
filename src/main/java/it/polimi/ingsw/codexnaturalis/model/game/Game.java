package it.polimi.ingsw.codexnaturalis.model.game;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.model.game.state.InitState;
import it.polimi.ingsw.codexnaturalis.model.game.state.State;
import it.polimi.ingsw.codexnaturalis.network.RMI.VirtualView;
// TODO: update view when model changes 

public class Game {
    private int gameId;
    private State gameState;
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
    final List<VirtualView> clients = new ArrayList<>();

    public Game(int gameId) {
        this.gameId = gameId;
        this.gameState = new InitState(this);
        this.players = new ArrayList<>();
        this.playerHand = new ArrayList<>();
        this.playerStructure = new ArrayList<>();
    }

    public void notifyAllObservers() {
        // ...
    }

    public void addObserver(VirtualView client) {
        clients.add(client);
    }

    public void removeObserver(VirtualView client) {
        clients.remove(client);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Player getNextPlayer() {
        return nextPlayer;
    }

    public State getState() {
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

    public void setLastTurn() {
        this.lastTurn = true;
        this.turnCounter = players.size() + (players.size() - players.indexOf(currentPlayer) + 1);
    }

    public void setState(State state) {
        this.gameState = state;
    }

    public void setNumPlayers(int numPlayers) {
        // FIXME: do this in initState
        for (int i = 0; i < numPlayers - 1; i++) {
            this.players.add(new Player());
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

    public void setNextPlayer(Player nextPlayer) {
        this.nextPlayer = nextPlayer;
    }

    public void calcStairPattern() {

    }

    public void calcChairPattern() {

    }

    public void throwException(String message) {
        // update client view with message
        // client.updateError(message);
    }

}
