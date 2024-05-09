package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class PlaceEvent extends Event {
    @Serial
    private static final long serialVersionUID = 693215087341982L;

    private Integer gameId;
    private String state;
    private List<Structure> playerStructure;
    private List<Hand> hands;
    private Board board;

    public PlaceEvent(Integer gameId, String state, List<Structure> playerStructure, List<Hand> hands, Board board) {
        this.gameId = gameId;
        this.state = state;
        this.playerStructure = playerStructure;
        this.hands = hands;
        this.board = board;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setState(state);
        miniModel.setPlayerStructure(playerStructure);
        miniModel.setHands(hands);
        miniModel.setBoard(board);
    }

    @Override
    public int getGameId() {
        return gameId;
    }
}