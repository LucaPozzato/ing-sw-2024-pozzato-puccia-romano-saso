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

/**
 * Abstract class representing a state in the game controller's state machine.
 * Different states handle different phases of the game and interact with
 * clients accordingly.
 */
public abstract class ControllerState {

    protected Game game;
    protected RmiServer rmiServer;
    protected SocketServer socketServer;

    /**
     * Constructs a new ControllerState with the provided game and server instances.
     *
     * @param game         The current game instance.
     * @param rmiServer    The RMI server instance.
     * @param socketServer The Socket server instance.
     */
    public ControllerState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        this.game = game;
        this.rmiServer = rmiServer;
        this.socketServer = socketServer;
    }

    public abstract void initialized(String clientId, String nick, String password, Color color, int numPlayers);

    public abstract void joinGame(String clientId, String nickname, String password, Color color);

    public abstract void chooseSetUp(String clientId, Player player, Boolean side, ObjectiveCard objCard);

    public abstract void placedCard(String clientId, Player player, Card father, Card placeThis, String position,
                                    Boolean frontUp);

    public abstract void drawnCard(String clientId, Player player, Card card, String fromDeck);

    public abstract void rejoinGame(String clientId, String nickname, String password);

    public abstract void disconnect(String clientId);

    /**
     * Updates the game's chat with a new message and sends a chat event to all
     * clients.
     *
     * @param clientId The client identifier.
     * @param message  The message content.
     * @param sender   The player sending the message.
     * @param receiver The intended receiver of the message (can be null for
     *                 broadcast).
     */
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
                if (sender != null && p.getNickname().equals(sender.getNickname())) {
                    senderPlayer = p;
                }
            }

            // FIXME: is it really necessary? Can it happens to make null sender? In that case does the game crash?
            if (senderPlayer == null) {
                throw new IllegalCommandException("The message was sent by a null sender. Game's over.");
            }

            if (receiverPlayer == null) {
                game.pushEvent("receiver not found");
            }

            chat.addMessage(new ChatMessage(message, senderPlayer, receiverPlayer, 0));
            event = new ChatEvent(game.getGameId(), chat);

        } catch (IllegalCommandException e) {
            this.game.pushEvent(e.getMessage());
            e.printStackTrace();
        }

        rmiServer.sendEvent(event);
        try {
            socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}