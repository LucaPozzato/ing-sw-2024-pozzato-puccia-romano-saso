package it.polimi.ingsw.codexnaturalis.model.events.updategameevent;

import it.polimi.ingsw.codexnaturalis.model.enumerations.StructureEventType;
import it.polimi.ingsw.codexnaturalis.model.events.Event;
import it.polimi.ingsw.codexnaturalis.view.View;

public class GameStructureUpdateEvent extends Event {

    private StructureEventType type;

    public void createGameStructureUpdateEvent(View structureView) {
    }
}