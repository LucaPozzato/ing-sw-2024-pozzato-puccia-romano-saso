package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serial;

public class Ping extends Command {
    @Serial
    private static final long serialVersionUID = 681111347560138L;
    private String clientId;

    public Ping(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }
}
