package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class EndGameEvent extends Event {
    @Serial
    private static final long serialVersionUID = 819362547038261L;

    private Integer gameId;
    private String state;
    private Board board;
    private List<Player> winner;

    public EndGameEvent(Integer gameId, String state, Board board, List<Player> winner) {
        this.gameId = gameId;
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

    @Override
    public int getGameId() {
        return gameId;
    }
}
