package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

import java.io.Serial;

public class EndGameEvent extends Event {
    @Serial
    private static final long serialVersionUID = 819362547038261L;
    private Board board;
    private Player winner;

    public EndGameEvent(Board board, Player winner) {
        this.board = board;
        this.winner = winner;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setBoard(board);
        miniModel.setWinner(winner);
    }
}