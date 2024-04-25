package it.polimi.ingsw.codexnaturalis.network.events;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

//extends Serializable? 
public class JoinGameEvent extends Event {
    String nickName;
    Color color;

    public JoinGameEvent(String nickName, Color color) {
        this.nickName = nickName;
        this.color = color;
    }

    public void run(ControllerState controller) throws IllegalCommandException {
        controller.joinGame(nickName, color);
    }
}