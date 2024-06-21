package it.polimi.ingsw.codexnaturalis.model.game.Chat;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.chat.ChatMessage;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChatTest {
    @Test
    void FlowChatTest(){
        Player sender = new Player();
        Player receiver = new Player();
        long l = 109283746501143L;

        ChatMessage chatMessage = new ChatMessage("prova", sender, receiver, l);

        assertEquals("prova", chatMessage.getMessage());
        assertEquals(sender, chatMessage.getSender());
        assertEquals(receiver, chatMessage.getReceiver());
        assertEquals(l, chatMessage.getTimeStamp());

        Chat chat = new Chat();
        chat.addMessage(chatMessage);
        assertEquals("prova", chat.getChatMessages().getFirst().getMessage());
        assertEquals(1, chat.getChatMessages().size());

        long l2 = 119283746501143L;
        ChatMessage secondChatMessage = new ChatMessage("prova2", sender, receiver, l2);

        chat.addMessage(secondChatMessage);
        assertEquals("prova", chat.getChatMessages().getFirst().getMessage());
        assertEquals("prova2", chat.getChatMessages().get(1).getMessage());
        assertEquals(2, chat.getChatMessages().size());

        chat.removeMessage(secondChatMessage);
        assertEquals("prova", chat.getChatMessages().getFirst().getMessage());
        assertEquals(1, chat.getChatMessages().size());
    }
}
