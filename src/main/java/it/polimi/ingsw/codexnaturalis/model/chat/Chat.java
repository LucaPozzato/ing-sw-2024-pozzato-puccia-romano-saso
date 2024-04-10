package it.polimi.ingsw.codexnaturalis.model.chat;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Chat {
    int nextId = 0;

    private List<Pair<Integer, ChatMessage>> chatMessages;

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

    public String draw(String player) {
        String chat = "";
        String receivers = "";
        for (Pair<Integer, ChatMessage> pair : chatMessages) {
            if (pair.getValue().getReceiver() == null || pair.getValue().getReceiver().getNickname().equals(player)) {
                if (pair.getValue().getReceiver() == null)
                    receivers = "everyone";
                else
                    receivers = pair.getValue().getReceiver().getNickname();
                chat += "\u001B[38;5;246m<from: " + pair.getValue().getSender().getNickname() + "> <to: " + receivers
                        + ">\u001B[0m " + pair.getValue().getMessage() + "\n";
            }
        }
        return chat;
    }
}