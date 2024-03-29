package it.polimi.ingsw.codexnaturalis.model.game;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.model.game.state.State;
import it.polimi.ingsw.codexnaturalis.view.View;

public abstract class Game {

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
    private List<View> observerList;

    public Game(int gameId) {
        this.gameId = gameId;
    }

    public void notifyAllObservers() {
        // ...
    }

    public void addObserver(View view) {
        observerList.add(view);
    }

    public void removeObserver(View view) {
        observerList.remove(view);
    }

    public void addPlayer(Player player) {
        players.add(player);
    }

    public Player getCurrentPlater() {
        return currentPlayer;
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

    public void setState(State state) {
        this.gameState = state;
    }

    public void setNumPlayers(int numPlayers) {
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

}
