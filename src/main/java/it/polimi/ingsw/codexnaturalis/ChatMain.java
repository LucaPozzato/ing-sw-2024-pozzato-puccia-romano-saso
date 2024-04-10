package it.polimi.ingsw.codexnaturalis;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.chat.ChatMessage;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.view.ChatCli;

public class ChatMain {
    public static void main(String[] args) {
        BufferedReader stdin = new BufferedReader(System.console().reader());
        Chat chat = new Chat();
        ChatCli chatCli = new ChatCli(stdin);
        List<Player> players = new ArrayList<Player>(
                List.of(new Player("nick"), new Player("giacomo"), new Player("mario"), new Player("luca")));
        Random random = new Random();
        Boolean randomReceiver;
        int randomIndexSender;
        Player randomReceivers;

        for (int i = 0; i < 100; i++) {
            randomReceiver = random.nextBoolean();
            randomIndexSender = random.nextInt(players.size());
            if (randomReceiver)
                randomReceivers = players.get(random.nextInt(players.size()));
            else
                randomReceivers = null;

            chat.addMessage(new ChatMessage(
                    "message message message message message message message message message message " + i,
                    players.get(randomIndexSender), randomReceivers,
                    System.currentTimeMillis()));
        }

        chatCli.updateChat(chat.draw("luca"));
        chatCli.print();
    }
}
