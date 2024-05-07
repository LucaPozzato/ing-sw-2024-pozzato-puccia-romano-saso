package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class ErrorEvent extends Event {
    private String error;

    public ErrorEvent(String error) {
        this.error = error;
    }

    @Override
    public void doJob(MiniModel miniModel) {
        miniModel.setError(error);
    }
}
