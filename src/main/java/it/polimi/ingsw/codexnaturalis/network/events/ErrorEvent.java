package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class ErrorEvent extends Event {
    private String clientId;
    private Integer gameId;
    private String error;

    public ErrorEvent(String clientId, Integer gameId, String error) {
        this.clientId = clientId;
        this.gameId = gameId;
        this.error = error;
    }

    @Override
    public void doJob(MiniModel miniModel) {
        miniModel.setError(error);
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }

    @Override
    public int getGameId() {
        return gameId;
    }
}
