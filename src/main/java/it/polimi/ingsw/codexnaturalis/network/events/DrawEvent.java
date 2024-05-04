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
    private Board board;
    private List<Hand> hands;

    public DrawEvent(List<Hand> hands, Board board, Integer turnCounter) {
        this.board = board;
        this.hands = hands;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setBoard(board);
        miniModel.setHands(hands);
    }

}