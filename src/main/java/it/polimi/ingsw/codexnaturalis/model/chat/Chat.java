package it.polimi.ingsw.codexnaturalis.model.chat;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javafx.util.Pair;

/**
 * Represents the chat of the game storaging the messages.
 * Each message is associated with a unique identifier.
 */
public class Chat implements Serializable {
    @Serial
    private static final long serialVersionUID = 109283746501965L;

    private int nextId = 0;

    private List<Pair<Integer, ChatMessage>> chatMessages;

    /**
     * Constructs a new Chat instance with an empty list of messages.
     */
    public Chat() {
        chatMessages = new ArrayList<Pair<Integer, ChatMessage>>();
    }

    public List<ChatMessage> getChatMessages() {
        List<ChatMessage> returnList = new ArrayList<ChatMessage>();
        for (Pair<Integer, ChatMessage> pair : chatMessages) {
            returnList.add(pair.getValue());
        }
        return returnList;
    }

    public void addMessage(ChatMessage message) {
        chatMessages.add(new Pair<Integer, ChatMessage>(nextId, message));
        nextId++;
    }

    public void removeMessage(ChatMessage message) {
        for (Pair<Integer, ChatMessage> pair : chatMessages) {
            if (pair.getValue().equals(message)) {
                chatMessages.remove(pair);
                return;
            }
        }
    }
}