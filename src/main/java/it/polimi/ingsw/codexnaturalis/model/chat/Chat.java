package it.polimi.ingsw.codexnaturalis.model.chat;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    private List<ChatMessage> chatMessages;

    public Chat() {
        chatMessages = new ArrayList<ChatMessage>();
    }

    public List<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public void addMessage(ChatMessage message) {
        chatMessages.add(message);
    }

    public void removeMessage(ChatMessage message) {
        chatMessages.remove(message);
    }
}