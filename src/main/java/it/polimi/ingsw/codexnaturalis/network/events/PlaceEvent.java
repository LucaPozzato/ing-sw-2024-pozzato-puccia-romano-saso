package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

//extends Serializable? 
public class PlaceEvent extends Event {
    Player player;
    Card father;
    Card placeThis;
    String position;
    Boolean frontUp;

    public PlaceEvent(Player player, Card father, Card placeThis, String position, Boolean frontUp) {
        this.player = player;
        this.father = father;
        this.placeThis = placeThis;
        this.position = position;
        this.frontUp = frontUp;
    }

    public void run(ControllerState controller) throws IllegalCommandException {
        controller.placedCard(player, father, placeThis, position, frontUp);
    }
}