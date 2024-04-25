package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

//extends Serializable? 
public class DrawEvent extends Event {
    Player player;
    Card card;
    String fromDeck;

    public DrawEvent(Player player, Card card, String fromDeck) {
        this.player = player;
        this.card = card;
        this.fromDeck = fromDeck;
    }

    public void run(ControllerState controller) throws IllegalCommandException {
        controller.drawnCard(player, card, fromDeck);
    }

}