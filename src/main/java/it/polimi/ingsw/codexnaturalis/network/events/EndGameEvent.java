package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.MiniModel;

public class EndGameEvent extends Event {
    private Board board;
    private Player winner;

    public EndGameEvent(Board board, Player winner) {
        this.board = board;
        this.winner = winner;
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setBoard(board);
        miniModel.setWinner(winner);
    }
}