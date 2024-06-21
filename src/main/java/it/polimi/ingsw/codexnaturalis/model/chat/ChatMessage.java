package it.polimi.ingsw.codexnaturalis.model.chat;

import java.io.Serial;
import java.io.Serializable;

import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

/**
 * Represents a message in the game chat.
 * Each message contains content, sender, receiver (optional), and timestamp.
 */
public class ChatMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 109283746501143L;

    private String message;
    private Player sender;
    private Player receiver;
    private long timeStamp;

    public ChatMessage(String message, Player sender, Player receiver, long timeStamp) {
        this.message = message;
        this.sender = sender;
        // if receiver is null -> the message is sent to everyone
        this.receiver = receiver;
        this.timeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public Player getSender() {
        return sender;
    }

    public Player getReceiver() {
        return receiver;
    }

    public long getTimeStamp() {
        return timeStamp;
    }
}