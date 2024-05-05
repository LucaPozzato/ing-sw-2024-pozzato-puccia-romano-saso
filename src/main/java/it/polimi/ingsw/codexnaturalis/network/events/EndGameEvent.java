package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

import java.io.Serial;

public class EndGameEvent extends Event {
    @Serial
    private static final long serialVersionUID = 819362547038261L;
    private String state;
    private Board board;
    private Player winner;

    public EndGameEvent(String state, Board board, Player winner) {
        this.state = state;
        this.board = board;
        this.winner = winner;
        this.state = state;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setBoard(board);
        miniModel.setWinner(winner);
        miniModel.setState(state);
    }
}