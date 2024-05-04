package it.polimi.ingsw.codexnaturalis.network.client;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class MiniModel {

    // Clients update will call the setters to save the changes
    // View will be notified of the changes and act consequently

    private int gameId;
    private Player myPlayer;
    private List<Player> players;
    private List<Hand> playerHands;
    // private List<Hand> playerHands; //BUG: if I pass playerHands to the client, a
    // "bad" client could see the cards and cheat
    private List<Structure> playerStructures;
    private Board board;
    private Player currentPlayer;
    private Deck deck;
    // Deck deck -> //BUG: if I pass deck to the client, a "bad" client could see
    // all the cards and cheat
    private Player nextPlayer;
    private Boolean lastTurn = false;
    private Integer turnCounter = 0;
    private Player winner;

    public MiniModel(int gameId, Player player) {
        this.gameId = gameId;
        this.myPlayer = player;
        this.players = new ArrayList<>();
        this.playerHands = new ArrayList<>();
        this.playerStructures = new ArrayList<>();
    }

    // might be handy for the interface
    public boolean isMyTurn() {
        return currentPlayer.equals(myPlayer);
    }

    // getter
    public int getGameId() {
        return this.gameId;
    }

    public Player getMyPlayer() {
        return this.myPlayer;
    }

    public Structure getMyPlayerStructure() {
        return this.playerStructures.get(players.indexOf(myPlayer));
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Hand getMyPlayerHand() {
        return this.playerHands.get(players.indexOf(myPlayer));
    }

    public List<Structure> getPlayerStructures() {
        return this.playerStructures;
    }

    public List<Hand> getPlayerHands() {
        return this.playerHands;
    }

    public Board getBoard() {
        return this.board;
    }

    public Deck getDeck() {
        return this.deck;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    public Player getNextPlayer() {
        return this.nextPlayer;
    }

    public Boolean getLastTurn() {
        return this.lastTurn;
    }

    public Integer getTurnCounter() {
        return this.turnCounter;
    }

    public Player getWinner() {
        return this.winner;
    }

    // setter

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setPlayerStructure(List<Structure> playerStructures) {
        this.playerStructures = playerStructures;
    }

    public void setHands(List<Hand> hands) {
        this.playerHands = hands;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    public void setNextPlayer(Player player) {
        this.nextPlayer = player;
    }

    public void setLastTurn(boolean lastTurn) {
        this.lastTurn = lastTurn;
    }

    public void setTurnCounter(Integer turnCounter) {
        this.turnCounter = turnCounter;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

}
