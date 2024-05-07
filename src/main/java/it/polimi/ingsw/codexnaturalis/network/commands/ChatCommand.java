package it.polimi.ingsw.codexnaturalis.network.commands;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

import java.io.Serial;

public class ChatCommand extends Command {
    @Serial
    private static final long serialVersionUID = 38011127530291L;

    private String message;
    private Player sender;
    private Player receiver;

    //TODO: can the view get the timeStamp information?
    //private long timeStamp;

    public ChatCommand (String message, Player sender, Player receiver/*, long timeStamp*/) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        //this.timeStamp = timeStamp;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        //TODO: controller.text(message, sender, receiver);
        // the method of the controller has to create a new ChatMessage with these information and add it to the chat
        // then create a new Event with the whole chat and send it
        // it should be possible to send messages in each state but for the IniState and maybe the EndGameState
    }
}