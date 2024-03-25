package it.polimi.ingsw.codexnaturalis.model.events.updategameevent;

import it.polimi.ingsw.codexnaturalis.model.enumerations.BoardEventType;
import it.polimi.ingsw.codexnaturalis.model.events.Event;
import it.polimi.ingsw.codexnaturalis.view.View;

public class GameBoardUpdateEvent extends Event {

    private BoardEventType type;

    public void createGameBoardUpdateEvent(View boardView) {
    }
}