package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.events.ChooseEvent;
import it.polimi.ingsw.codexnaturalis.network.events.ErrorEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.ForcedEndEvent;
import it.polimi.ingsw.codexnaturalis.network.events.InLobbyEvent;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

/**
 * Represents the state of the game where the game is waiting for players to
 * join.
 */
public class WaitPlayerState extends ControllerState {

    /**
     * Constructs a new WaitPlayerState.
     *
     * @param game         The game instance.
     * @param rmiServer    The RMI server instance.
     * @param socketServer The socket server instance.
     */
    public WaitPlayerState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        super(game, rmiServer, socketServer);
    }

    /**
     * Sends an error event indicating that the game has already been initialized,
     * preventing redundant initialization attempts.
     *
     * @param clientId   The client ID initiating the action.
     * @param nick       The nickname of the player attempting to initialize.
     * @param password   The password provided by the player.
     * @param color      The color chosen by the player.
     * @param numPlayers The number of players in the game.
     */
    @Override
    public void initialized(String clientId, String nick, String password, Color color, int numPlayers) {
        String error = "Game already initialized";
        super.game.pushEvent(error);
        Event event = new ErrorEvent(clientId, game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
    }

    /**
     * Handles a player attempting to join the game.
     *
     * @param clientId The client ID attempting to join the game.
     * @param nickname The nickname of the player.
     * @param password The password of the player.
     * @param color    The color chosen by the player.
     */
    @Override
    public void joinGame(String clientId, String nickname, String password, Color color) {
        try {
            if (nickname.isEmpty()) {
                throw new IllegalCommandException("Nickname can't be empty");
            }

            for (Player p : super.game.getPlayers()) {
                if (p.getColor() != null && p.getColor().equals(color)) {
                    throw new IllegalCommandException("Color already taken");
                }
                if (p.getNickname() != null && p.getNickname().equals(nickname)) {
                    throw new IllegalCommandException("Nickname already taken");
                }
            }

            if (isFull()) {
                throw new IllegalCommandException("Game already full");
            }

            createNewPlayers(clientId, nickname, password, color);

        } catch (IllegalCommandException err) {
            System.out.println("> sent error: " + err.getMessage());
            super.game.pushEvent(err.getMessage());

            Event event = new ErrorEvent(clientId, game.getGameId(), err.getMessage());
            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                String noSer = "No server found";
                super.game.pushEvent(noSer);
                e.printStackTrace();
            }
        }
    }

    /**
     * Sends an error event indicating that the game setup has already been
     * completed,
     * preventing redundant setup attempts.
     *
     * @param clientId The client ID attempting to set up the game.
     * @param player   The player initiating the setup.
     * @param side     The side chosen by the player.
     * @param objCard  The objective card chosen by the player.
     */
    @Override
    public void chooseSetUp(String clientId, Player player, Boolean side, ObjectiveCard objCard) {
        String error = "Game not set up yet";
        super.game.pushEvent(error);
        Event event = new ErrorEvent(clientId, game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
    }

    /**
     * Creates a new player and updates the game state accordingly.
     *
     * @param clientId The client ID of the player.
     * @param nickname The nickname of the player.
     * @param password The password of the player.
     * @param color    The color chosen by the player.
     */
    private void createNewPlayers(String clientId, String nickname, String password, Color color) {
        Player player = super.game.getPlayers().get(super.game.getNumParticipants());
        player.setNickname(nickname);
        player.setPassword(password);
        player.setColor(color);
        super.game.addParticipant();
        super.game.getFromPlayerToId().put(player, clientId);

        // FIXME: clean this up
        try {
            super.game.getBoard().updateActualScore(player, 0);
            // FIXME: this is a temporary solution
            // super.game.getBoard().updateActualScore(player, 19);
        } catch (IllegalCommandException e) {
            e.printStackTrace();
        }

        if (isFull()) {
            Event inLobbyEvent = new InLobbyEvent(clientId, super.game.getGameId(), "Wait", nickname);
            Event chooseEvent = new ChooseEvent(super.game.getGameId(), "Choose", super.game.getHands(),
                    super.game.getPlayers());
            super.rmiServer.sendEvent(inLobbyEvent);
            try {
                super.socketServer.sendEvent(inLobbyEvent);
            } catch (Exception e) {
                String noSer = "No server found";
                super.game.pushEvent(noSer);
                e.printStackTrace();
            }

            super.rmiServer.sendEvent(chooseEvent);
            try {
                super.socketServer.sendEvent(chooseEvent);
            } catch (Exception e) {
                String noSer = "No server found";
                super.game.pushEvent(noSer);
                e.printStackTrace();
            }

            super.game.setState(new ChooseSetUpState(super.game, super.rmiServer, super.socketServer, null));
        } else {
            Event event = new InLobbyEvent(clientId, super.game.getGameId(), "Wait", nickname);
            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                String noSer = "No server found";
                super.game.pushEvent(noSer);
                e.printStackTrace();
            }

            super.game.setState(new WaitPlayerState(super.game, super.rmiServer, super.socketServer));
        }
    }

    private boolean isFull() {
        return super.game.getPlayers().size() == super.game.getNumParticipants();
    }

    /**
     * Sends an error event indicating that the player cannot place a card at the
     * current moment.
     *
     * @param clientId  The client ID attempting to place the card.
     * @param player    The player attempting to place the card.
     * @param father    The card where the placement is initiated.
     * @param placeThis The card being placed.
     * @param position  The position where the card is placed.
     * @param frontUp   The orientation of the card.
     */
    @Override
    public void placedCard(String clientId, Player player, Card father, Card placeThis, String position,
            Boolean frontUp) {
        String error = "Can't place card yet";
        super.game.pushEvent(error);
        Event event = new ErrorEvent(clientId, game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
    }

    /**
     * Sends an error event indicating that the player cannot draw a card at the
     * current moment.
     *
     * @param clientId The client ID attempting to place the card.
     * @param player   The player attempting to place the card.
     * @param card     The card to draw
     * @param fromDeck The deck from where to draw
     */
    @Override
    public void drawnCard(String clientId, Player player, Card card, String fromDeck) {
        String error = "Can't draw card yet";
        super.game.pushEvent(error);
        Event event = new ErrorEvent(clientId, game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
    }

    /**
     * Handles disconnection of a client from the game shutting it down and sending
     * a ForcedEndEvent to the other players
     *
     * @param clientId The client identifier.
     */
    @Override
    public void disconnect(String clientId) {
        String error = "Game was shut down due to clients' disconnections";
        super.game.pushEvent(error);
        Event event = new ForcedEndEvent(game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
        super.game.setState(new ForcedEndState(super.game, super.rmiServer, super.socketServer));
    }

    /**
     * Handles a player's attempt to rejoin the game.
     *
     * @param clientId The client identifier.
     * @param nickname The nickname of the client.
     * @param password The password of the client.
     */
    @Override
    public void rejoinGame(String clientId, String nickname, String password) {
        String error = "Can't rejoin game now";
        super.game.pushEvent(error);
        Event event = new ErrorEvent(clientId, game.getGameId(), error);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
    }

}