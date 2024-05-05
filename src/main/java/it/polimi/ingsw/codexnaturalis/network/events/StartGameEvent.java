package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class StartGameEvent extends Event {
    @Serial
    private static final long serialVersionUID = 820945163792530L;
    private String state;
    private List<Player> players;
    private List<Structure> playerStructure;
    private List<Hand> hands;
    private Board board;
    private Player currentPlayer;
    private Player nextPlayer;

    public StartGameEvent(String state, List<Player> players, List<Structure> playerStructure, List<Hand> hands,
            Board board,
            Player currentPlayer,
            Player nextPlayer) {
        this.state = state;
        this.players = players;
        this.playerStructure = playerStructure;
        this.hands = hands;
        this.board = board;
        this.currentPlayer = currentPlayer;
        this.nextPlayer = nextPlayer;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setPlayers(players);
        miniModel.setPlayerStructure(playerStructure);
        miniModel.setHands(hands);
        miniModel.setBoard(board);
        miniModel.setCurrentPlayer(currentPlayer);
        miniModel.setNextPlayer(nextPlayer);
        miniModel.setState(state);
    }
}