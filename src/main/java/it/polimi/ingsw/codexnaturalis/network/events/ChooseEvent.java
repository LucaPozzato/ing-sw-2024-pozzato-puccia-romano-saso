package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class ChooseEvent extends Event {
    @Serial
    private static final long serialVersionUID = 432103916472093L;
    private Integer gameId;
    private String state;
    private List<Hand> hands;
    private List<Player> players;

    public ChooseEvent(Integer gameId, String state, List<Hand> hands, List<Player> players) {
        this.gameId = gameId;
        this.state = state;
        this.hands = hands;
        this.players = players;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setPlayers(players);
        miniModel.setHands(hands);
        miniModel.setState(state);
    }

    @Override
    public String getClientId() {
        return null;
    }

    @Override
    public int getGameId() {
        return gameId;
    }
}