package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class PlaceCommand extends Command {
    @Serial
    private static final long serialVersionUID = 293847506109283L;
    private String clientId;

    private Integer gameId;
    private Player player;
    private Card father;
    private Card placeThis;
    private String position;
    private Boolean frontUp;

    public PlaceCommand(String clientId, Integer gameId, Player player, Card father, Card placeThis, String position, Boolean frontUp) {
        this.clientId = clientId;
        this.gameId = gameId;
        this.player = player;
        this.father = father;
        this.placeThis = placeThis;
        this.position = position;
        this.frontUp = frontUp;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        controller.placedCard(clientId, player, father, placeThis, position, frontUp);
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