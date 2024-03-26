package it.polimi.ingsw.codexnaturalis.model.events.updategameevent;

import it.polimi.ingsw.codexnaturalis.model.enumerations.DeckEventType;
import it.polimi.ingsw.codexnaturalis.model.events.Event;
import it.polimi.ingsw.codexnaturalis.view.View;

public class GameDeckUpdateEvent extends Event {

    private DeckEventType type;

    public void createGameDeckUpdateEvent(View deckView) {
    }
}