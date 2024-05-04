package it.polimi.ingsw.codexnaturalis.network.commands;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

import java.io.Serial;

public class ChooseCommand extends Command {
    @Serial
    private static final long serialVersionUID = 923874056382019L;
    Player player;
    Boolean side;
    ObjectiveCard objCard;

    public ChooseCommand(Player player, Boolean side, ObjectiveCard objCard) {
        this.player = player;
        this.side = side;
        this.objCard = objCard;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        controller.chooseSetUp(player, side, objCard);
    }
}