package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public class ReJoinCommand extends Command {
    @Serial
    private static final long serialVersionUID = 681033347560138L;
    private String clientId;
    private String nickName;
    private String password;
    private Integer gameId;

    public ReJoinCommand(String clientId, Integer gameId, String nickName, String password) {
        this.clientId = clientId;
        this.nickName = nickName;
        this.password = password;
        this.gameId = gameId;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        controller.reJoinGame(gameId, nickName, password);
    }

    @Override
    public int getGameId() {
        return this.gameId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

    @Override
    public String getPassword() {
        return password;
    }

}