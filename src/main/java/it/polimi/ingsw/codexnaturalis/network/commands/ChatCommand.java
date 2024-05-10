package it.polimi.ingsw.codexnaturalis.network.commands;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.controller.ControllerState;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class ChatCommand extends Command {
    @Serial
    private static final long serialVersionUID = 38011127530291L;
    private String clientId;
    private Integer gameId;
    private String message;
    private Player sender;
    private Player receiver;

    // TODO: can the view get the timeStamp information?
    // private long timeStamp;

    public ChatCommand(String clientId, Integer gameId, String message, Player sender, Player receiver) {
        this.clientId = clientId;
        this.gameId = gameId;
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        // this.timeStamp = timeStamp;
    }

    @Override
    public void execute(ControllerState controller) throws IllegalCommandException {
        controller.updateChat(clientId, message, sender, receiver);
    }

    @Override
    public int getGameId() {
        return this.gameId;
    }

    @Override
    public String getClientId() {
        return clientId;
    }

}