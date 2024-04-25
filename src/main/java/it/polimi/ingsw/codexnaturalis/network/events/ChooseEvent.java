package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serializable;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public abstract class ChooseEvent implements Serializable {
    Player player;
    Boolean side;
    ObjectiveCard objCard;

    // TODO: @Serial

    public ChooseEvent(Player player, Boolean side, ObjectiveCard objCard) {
        this.player = player;
        this.side = side;
        this.objCard = objCard;
    }

    public void run(ControllerState controller) throws IllegalCommandException {
        controller.chooseSetUp(player, side, objCard);
    }
}