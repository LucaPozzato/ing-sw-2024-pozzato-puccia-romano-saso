package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public class CreateGameCommand extends Command {
    @Serial
    private static final long serialVersionUID = 380164227530291L;
    private String clientId;
    private String nickName;
    private Color color;
    private Integer gameId;
    private Integer numOfPlayers;

    public CreateGameCommand(String clientId, Integer gameId, String nickName, Color color, Integer numOfPlayers) {
        this.clientId = clientId;
        this.nickName = nickName;
        this.color = color;
        this.gameId = gameId;
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        controller.initialized(clientId, nickName, color, numOfPlayers);
    }

    @Override
    public int getGameId() {
        return this.gameId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }
}
