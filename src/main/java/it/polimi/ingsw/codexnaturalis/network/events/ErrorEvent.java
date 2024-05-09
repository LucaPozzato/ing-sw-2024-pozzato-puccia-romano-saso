package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class ErrorEvent extends Event {
    private Integer gameId;
    private String error;

    public ErrorEvent(Integer gameId, String error) {
        this.gameId = gameId;
        this.error = error;
    }

    @Override
    public void doJob(MiniModel miniModel) {
        miniModel.setError(error);
    }

    @Override
    public int getGameId() {
        return gameId;
    }
}
