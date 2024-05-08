package it.polimi.ingsw.codexnaturalis.network.client;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.view.View;

/**
 * this clas serves as a local copy of the model on the client side that
 * contains only the needed pieces of information and none of the model logic
 * it is updated to by the events, sent from the server, and for every update
 * it calls the viewUpdate methods to modify the view
 */
public class MiniModel {

    private int gameId;
    private Player myPlayer;
    private String nickname;
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
    private List<Player> winner;
    private Chat chat;
    private String error;
    private String state;
    private View view;

    public MiniModel() {
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

    public List<Player> getWinner() {
        return this.winner;
    }

    public String getError() {
        return this.error;
    }

    public String getState() {
        return this.state;
    }

    public Chat getChat() {
        return this.chat;
    }

    // setter

    public void setView(View view) {
        this.view = view;
    }

    public void setState(String state) {
        this.state = state;
        view.updateState(state);
    }

    public void setError(String error) {
        this.error = error;
        view.updateError(error);
    }

    public void setChat(Chat chat) {
        this.chat = chat;
        view.updateChat(chat);
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public void setMyPlayer(String myPlayer) {
        // BUG: everyone is gonna get the same nickname when joining simultaneously
        this.nickname = myPlayer;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
        view.updatePlayers(players);
        // BUG: players should not have nickname == null
        for (Player p : players) {
            if (p != null && p.getNickname() != null && p.getNickname().equals(nickname)) {
                this.myPlayer = p;
                view.updateMyPlayer(p);
                break;
            }
        }
    }

    public void setPlayerStructure(List<Structure> playerStructures) {
        this.playerStructures = playerStructures;
        view.updateStructures(playerStructures);
    }

    public void setHands(List<Hand> hands) {
        this.playerHands = hands;
        view.updateHand(hands);
    }

    public void setBoard(Board board) {
        this.board = board;
        view.updateBoard(board);
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
        view.updateDeck(deck);
    }

    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
        view.updateCurrentPlayer(player);
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

    public void setWinner(List<Player> winner) {
        this.winner = winner;
        view.updateWinners(winner);
    }

}
