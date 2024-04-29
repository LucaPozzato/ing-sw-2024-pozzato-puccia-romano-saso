package it.polimi.ingsw.codexnaturalis.network.commands;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class DrawCommand extends Command {
    Player player;
    Card card;
    String fromDeck;

    public DrawCommand(Player player, Card card, String fromDeck) {
        this.player = player;
        this.card = card;
        this.fromDeck = fromDeck;
    }

    public void execute(ControllerState controller) throws IllegalCommandException {
        controller.drawnCard(player, card, fromDeck);
    }

}