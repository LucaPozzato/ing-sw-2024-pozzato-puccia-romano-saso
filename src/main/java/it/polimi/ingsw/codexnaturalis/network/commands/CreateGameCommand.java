package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public class CreateGameCommand extends Command {
    @Serial
    // private static final long serialVersionUID = 380164927530291L;
    String nickName;
    Color color;
    Integer gameId;
    Integer numOfPlayers;

    public CreateGameCommand(Integer gameId, String nickName, Color color, Integer numOfPlayers) {
        this.nickName = nickName;
        this.color = color;
        this.gameId = gameId;
        this.numOfPlayers = numOfPlayers;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        // controller.createGame
        // FIXME: gameID should be used in case of multiple games
    }
}
