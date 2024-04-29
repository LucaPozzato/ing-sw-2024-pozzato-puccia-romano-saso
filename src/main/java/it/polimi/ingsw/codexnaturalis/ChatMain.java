package it.polimi.ingsw.codexnaturalis;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.chat.ChatMessage;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.view.tui.ChatCli;

public class ChatMain {
    public static void main(String[] args) {
        BufferedReader stdin = new BufferedReader(System.console().reader());
        Chat chat = new Chat();
        ChatCli chatCli = new ChatCli(stdin);
        List<Player> players = new ArrayList<Player>(
                List.of(new Player("nick"), new Player("giacomo"), new Player("mario"), new Player("luca")));
        Random random = new Random();
        int randomIndexSender;
        Player sender = null;
        Player receiver = null;
        List<String> cliDecision;

        for (int i = 0; i < 100; i++) {
            randomIndexSender = random.nextInt(players.size());
            if (random.nextBoolean())
                receiver = players.get(random.nextInt(players.size()));
            else
                receiver = null;

            // ChatMessage(String message, Player sender, Player receiver, long timeStamp)

            chat.addMessage(new ChatMessage("message " + i, players.get(randomIndexSender), receiver,
                    System.currentTimeMillis()));
            chatCli.updateChat(chat.draw("luca"));
            cliDecision = chatCli.print();
            for (Player player : players) {
                if (player.getNickname().equals("luca"))
                    sender = player;
                if (cliDecision.get(0).equals(player.getNickname()))
                    receiver = player;
                else if (cliDecision.get(0).equals(""))
                    receiver = null;
            }
            chat.addMessage(new ChatMessage(cliDecision.get(1), sender, receiver, System.currentTimeMillis()));
            chatCli.updateChat(chat.draw("luca"));
        }
    }
}
