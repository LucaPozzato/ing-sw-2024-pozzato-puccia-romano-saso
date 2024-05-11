package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.chat.ChatMessage;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.events.ChatEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

public abstract class ControllerState {

    protected Game game;
    protected RmiServer rmiServer;
    protected SocketServer socketServer;

    public ControllerState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        this.game = game;
        this.rmiServer = rmiServer;
        this.socketServer = socketServer;
    }

    public abstract void initialized(String clientId, String nick, Color color, int numPlayers);

    public abstract void joinGame(String clientId, String nickname, Color color);

    public abstract void chooseSetUp(String clientId, Player player, Boolean side, ObjectiveCard objCard);

    public abstract void placedCard(String clientId, Player player, Card father, Card placeThis, String position,
            Boolean frontUp);

    public abstract void drawnCard(String clientId, Player player, Card card, String fromDeck);

    public void updateChat(String clientId, String message, Player sender, Player receiver) {
        Chat chat = game.getChat();
        Player receiverPlayer = null;
        Player senderPlayer = null;
        Event event = null;

        try {
            for (Player p : game.getPlayers()) {
                if (receiver != null && p.getNickname().equals(receiver.getNickname())) {
                    receiverPlayer = p;
                }
                if (p.getNickname().equals(sender.getNickname())) {
                    senderPlayer = p;
                }
            }
            if (senderPlayer == null) {
                throw new IllegalCommandException("sender not found");
            }

            chat.addMessage(new ChatMessage(message, senderPlayer, receiverPlayer, 0));

            event = new ChatEvent(clientId, game.getGameId(), chat);
        } catch (IllegalCommandException e) {
            e.printStackTrace();
        }

        rmiServer.sendEvent(event);
        try {
            socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //public abstract void rejoin(String nickname);

}