package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

import java.io.Serial;

public class ForcedEndEvent extends Event {
    @Serial
    private static final long serialVersionUID = 819312347038261L;
    private Integer gameId;
    private String cause;

    public ForcedEndEvent(Integer gameId, String cause) {
        this.gameId = gameId;
        this.cause = cause;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setError(cause);
    }

    @Override
    public int getGameId() {
        return gameId;
    }

    @Override
    public String getClientId() {
        return null;
    }
}
