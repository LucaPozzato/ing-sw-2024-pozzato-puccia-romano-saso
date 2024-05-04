package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

import java.io.Serial;

public class DrawEvent extends Event {
    @Serial
    private static final long serialVersionUID = 402918736529103L;
    private Integer turnCounter;
    private Board board;

    public DrawEvent(Hand hand, Board board, Integer turnCounter) {
        this.board = board;
        this.turnCounter = turnCounter;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setBoard(board);
        miniModel.setTurnCounter(turnCounter);
    }

}