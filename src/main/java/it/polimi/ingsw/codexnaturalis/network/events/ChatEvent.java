package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class ChatEvent extends Event {
    @Serial
    private static final long serialVersionUID = 380111272220291L;

    private Chat chat;
    private Integer gameId;

    public ChatEvent(Integer gameId, Chat chat) {
        this.chat = chat;
    }

    @Override
    public void doJob(MiniModel minimodel) throws IllegalCommandException {
        minimodel.setChat(chat);
    }

    @Override
    public int getGameId() {
        return gameId;
    }
}