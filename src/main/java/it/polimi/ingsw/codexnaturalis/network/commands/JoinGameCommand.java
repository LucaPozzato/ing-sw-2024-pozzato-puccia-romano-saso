package it.polimi.ingsw.codexnaturalis.network.commands;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

import java.io.Serial;

public class JoinGameCommand extends Command {
    @Serial
    private static final long serialVersionUID = 681029347560138L;
    private String nickName;
    private Color color;
    private Integer gameId;

    public JoinGameCommand(Integer gameId, String nickName, Color color) {
        this.nickName = nickName;
        this.color = color;
        this.gameId = gameId;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        controller.joinGame(nickName, color);
    }

    public int getGameId(){
        return this.gameId;
    }
}