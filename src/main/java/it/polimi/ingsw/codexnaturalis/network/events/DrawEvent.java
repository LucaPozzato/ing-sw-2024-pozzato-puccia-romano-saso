package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

import java.io.Serial;
import java.util.List;

public class DrawEvent extends Event {
    @Serial
    private static final long serialVersionUID = 402918736529103L;
    private String state;
    private Board board;
    private List<Hand> hands;
    private Integer turnCounter;
    private boolean lastTurn;

    public DrawEvent(String state, List<Hand> hands, Board board, Integer turnCounter, boolean lastTurn) {
        this.board = board;
        this.hands = hands;
        this.state = state;
        this.turnCounter = turnCounter;
        this.lastTurn = lastTurn;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setBoard(board);
        miniModel.setHands(hands);
        miniModel.setState(state);
        miniModel.setTurnCounter(turnCounter);
        miniModel.setLastTurn(lastTurn);
    }
}