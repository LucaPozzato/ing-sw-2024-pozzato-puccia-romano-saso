package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public class JoinGameCommand extends Command {
    @Serial
    private static final long serialVersionUID = 681029347560138L;
    private String clientId;
    private String nickName;
    private String password;

    private Color color;
    private Integer gameId;

    public JoinGameCommand(String clientId, Integer gameId, String nickName, String password, Color color) {
        this.clientId = clientId;
        this.nickName = nickName;
        this.password = password;
        this.color = color;
        this.gameId = gameId;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        controller.joinGame(clientId, nickName, password, color);
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
    public String getPassword(){
        return password;
    }

}