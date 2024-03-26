package it.polimi.ingsw.codexnaturalis.model.events.updategameevent;

import it.polimi.ingsw.codexnaturalis.model.enumerations.HandEventType;
import it.polimi.ingsw.codexnaturalis.model.events.Event;
import it.polimi.ingsw.codexnaturalis.view.View;

public class GameHandUpdateEvent extends Event {

    private HandEventType type;

    public void createGameHandUpdateEvent(View handView) {
    }
}
