package it.polimi.ingsw.codexnaturalis.network;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class MiniModel {

    // Clients update will call the setters to save the changes
    // View will be notified of the changes and act consequently

    private int gameId;
    private Player myPlayer;
    private List<Player> players;
    private Hand playerHand;
    private List<Structure> playerStructure;
    private List<Card> uncoveredCards;
    private List<Card> commonObjectives;
    private Map<Player, Integer> actualScores;
    private Player currentPlayer;
    private Player nextPlayer;
    private Boolean lastTurn = false;
    private Integer turnCounter = 0;
    private Player winner;

    public MiniModel(int gameId, Player player) {
        this.gameId = gameId;
        this.myPlayer = player;
        this.players = new ArrayList<>();
        this.playerHand = new Hand();
        this.playerStructure = new ArrayList<>();
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
        return this.playerStructure.get(players.indexOf(myPlayer));
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Hand getPlayerHand() {
        return this.playerHand;
    }

    public List<Structure> getPlayerStructure() {
        return this.playerStructure;
    }

    public List<Card> getUncoveredCards() {
        return this.uncoveredCards;
    }

    public List<Card> getCommonObjectives() {
        return this.commonObjectives;
    }

    public Map<Player, Integer> getActualScores() {
        return this.actualScores;
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

    public void setPlayerStructure(List<Structure> playerStructure) {
        this.playerStructure = playerStructure;
    }

    public void setHand(Hand hand) {
        this.playerHand = hand;
    }

    public void setUncoveredCards(List<Card> uncoveredCards) {
        this.uncoveredCards = uncoveredCards;
    }

    public void setCommonObjectives(List<Card> commonObjectives) {
        this.commonObjectives = commonObjectives;
    }

    public void setActualScore(Map<Player, Integer> actualScores) {
        this.actualScores = actualScores;
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
