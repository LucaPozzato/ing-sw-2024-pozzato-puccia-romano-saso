package it.polimi.ingsw.codexnaturalis.network.commands;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

import java.io.Serial;

//extends Serializable? 
public class JoinGameCommand extends Command {
    @Serial
    private static final long serialVersionUID = 681029347560138L;
    String nickName;
    Color color;
    Integer gameId;

    public JoinGameCommand(Integer gameId, String nickName, Color color) {
        this.nickName = nickName;
        this.color = color;
        this.gameId = gameId;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        controller.joinGame(nickName, color);
        // FIXME: gameID should be used in case of multiple games
    }
}