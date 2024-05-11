package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class DrawEvent extends Event {
    @Serial
    private static final long serialVersionUID = 402918736529103L;

    private Integer gameId;
    private String state;
    private Board board;
    private List<Hand> hands;
    private Deck deck;
    private Player currentPlayer;
    private Integer turnCounter;
    private boolean lastTurn;

    public DrawEvent(Integer gameId, String state, List<Hand> hands, Player currentPlayer, Deck deck, Board board,
            Integer turnCounter,
            boolean lastTurn) {
        this.gameId = gameId;
        this.board = board;
        this.hands = hands;
        this.deck = deck;
        this.currentPlayer = currentPlayer;
        this.state = state;
        this.turnCounter = turnCounter;
        this.lastTurn = lastTurn;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setBoard(board);
        miniModel.setHands(hands);
        miniModel.setDeck(deck);
        miniModel.setCurrentPlayer(currentPlayer);
        miniModel.setState(state);
        miniModel.setTurnCounter(turnCounter);
        miniModel.setLastTurn(lastTurn);
    }

    @Override
    public int getGameId() {
        return gameId;
    }

    @Override
    public String getClientId() {
        return null;
    }
}