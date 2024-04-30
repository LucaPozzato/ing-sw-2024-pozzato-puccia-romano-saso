package it.polimi.ingsw.codexnaturalis.network.events;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.MiniModel;

public class StartGameEvent extends Event {
    private List<Player> players;
    private List<Structure> playerStructure;
    private Board board;
    private Player currentPlayer;
    private Player nextPlayer;

    public StartGameEvent(List<Player> players, List<Structure> playerStructure, Board board, Player currentPlayer,
            Player nextPlayer) {
        this.players = players;
        this.playerStructure = playerStructure;
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.nextPlayer = nextPlayer;
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setPlayers(players);
        miniModel.setPlayerStructure(playerStructure);
        miniModel.setBoard(board);
        miniModel.setCurrentPlayer(currentPlayer);
        miniModel.setNextPlayer(nextPlayer);
    }
}