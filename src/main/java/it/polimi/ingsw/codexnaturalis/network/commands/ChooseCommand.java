package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class ChooseCommand extends Command {
    @Serial
    private static final long serialVersionUID = 923874056382019L;
    private String clientId;

    private Integer gameId;
    private Player player;
    private Boolean side;
    private ObjectiveCard objCard;

    public ChooseCommand(String clientId, Integer gameId, Player player, Boolean side, ObjectiveCard objCard) {
        this.clientId = clientId;
        this.gameId = gameId;
        this.player = player;
        this.side = side;
        this.objCard = objCard;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        controller.chooseSetUp(clientId, player, side, objCard);
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