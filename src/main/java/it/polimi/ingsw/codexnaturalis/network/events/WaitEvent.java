package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class WaitEvent extends Event {
    @Serial
    private static final long serialVersionUID = 693215087341912L;
    private String clientId;
    private Integer gameId;
    private String state;

    public WaitEvent(String clientId, Integer gameId, String state) {
        this.clientId = clientId;
        this.gameId = gameId;
        this.state = state;
    }

    @Override
    public void doJob(MiniModel miniModel) {
        miniModel.setGameId(gameId);
        miniModel.setState(state);
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }
}
