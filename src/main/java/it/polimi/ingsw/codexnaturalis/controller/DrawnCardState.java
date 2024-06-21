package it.polimi.ingsw.codexnaturalis.controller;

import java.util.EmptyStackException;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.events.DrawEvent;
import it.polimi.ingsw.codexnaturalis.network.events.ErrorEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.ForcedEndEvent;
import it.polimi.ingsw.codexnaturalis.network.events.PlaceEvent;
import it.polimi.ingsw.codexnaturalis.network.events.RejoinGameEvent;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

/**
 * Represents a state where a player has drawn a card and the game is in
 * progress.
 * Handles actions related to drawing cards, updating game state, and managing
 * player turns.
 */
public class DrawnCardState extends ControllerState {
    boolean drawn = false;

    /**
     * Constructs a DrawnCardState with the given game and server instances.
     *
     * @param game         The current game instance.
     * @param rmiServer    The RMI server instance.
     * @param socketServer The Socket server instance.
     */
    public DrawnCardState(Game game, RmiServer rmiServer, SocketServer socketServer) {
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
        Event event = new ErrorEvent(clientId, game.getGameId(), "Game already initialized");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an error event indicating that the player has already joined the game,
     * preventing redundant join attempts.
     *
     * @param clientId The client ID attempting to join.
     * @param nickname The nickname of the player attempting to join.
     * @param password The password provided by the player.
     * @param color    The color chosen by the player.
     */
    @Override
    public void joinGame(String clientId, String nickname, String password, Color color) {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Game already joined");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an error event indicating that the game setup has already been
     * completed,
     * preventing redundant setup attempts.
     *
     * @param clientId The client ID attempting to set up the game.
     * @param nickname The player initiating the setup.
     * @param side     The side chosen by the player.
     * @param objCard  The objective card chosen by the player.
     */
    @Override
    public void chooseSetUp(String clientId, Player nickname, Boolean side, ObjectiveCard objCard) {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Game already set up");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
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

        Event event = null;
        if (!player.equals(super.game.getCurrentPlayer())) {
            event = new ErrorEvent(clientId, game.getGameId(), "Not your turn");
        } else {
            event = new ErrorEvent(clientId, game.getGameId(), "Can't place card now");
        }

        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event where a player draws a card from a specified deck or the
     * uncovered cards.
     * Updates the game state, sends appropriate events to clients, and transitions
     * to the next game state.
     *
     * @param clientId The client ID of the player drawing the card.
     * @param player   The player drawing the card.
     * @param card     The card drawn.
     * @param fromDeck The deck from which the card is drawn ("GOLD", "RESOURCE", or
     *                 empty for uncovered cards).
     */
    @Override
    public synchronized void drawnCard(String clientId, Player player, Card card, String fromDeck) {

        Event event = null;
        Boolean matchEnded = false;

        try {

            for (Player p : super.game.getPlayers()) {
                if (p.getNickname().equals(player.getNickname())) {
                    player = p;
                    break;
                }
            }

            if (!player.equals(super.game.getCurrentPlayer())) {
                throw new IllegalCommandException("Not your turn");
            }

            if (fromDeck == null || fromDeck.equals(""))
                for (Card c : super.game.getBoard().getUncoveredCards()) {
                    if (c.getIdCard().equals(card.getIdCard())) {
                        card = c;
                        break;
                    }
                }
            else
                card = null;

            updateDeck(card, fromDeck);
            matchEnded = nextTurn();

            event = new DrawEvent(game.getGameId(), "Place", game.getHands(), game.getCurrentPlayer(), game.getDeck(),
                    game.getBoard(), game.getTurnCounter(), game.isLastTurn());

            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (matchEnded)
                super.game.setState(new EndGameState(super.game, super.rmiServer, super.socketServer));
            else
                super.game.setState(new PlacedCardState(super.game, super.rmiServer, super.socketServer));

        } catch (IllegalCommandException e) {
            event = new ErrorEvent(clientId, game.getGameId(), e.getMessage());
        }

        drawn = true;
    }

    /**
     * Helper method to update the game deck based on the drawn card.
     *
     * @param card     The card drawn.
     * @param fromDeck The deck from which the card is drawn ("GOLD", "RESOURCE", or
     *                 null/empty for uncovered cards).
     * @throws IllegalCommandException If there are no cards left in the specified
     *                                 deck.
     */
    private void updateDeck(Card card, String fromDeck) throws IllegalCommandException {
        if (super.game.getDeck().getGoldDeck().isEmpty() && super.game.getDeck().getResourceDeck().isEmpty()
                && !super.game.isLastTurn()) {
            super.game.setLastTurn();
        }

        // if chosen deck is gold then draw from gold deck
        if (fromDeck.equals("GOLD")) {
            try {
                card = super.game.getDeck().drawGoldCard();
            } catch (EmptyStackException e) {
                throw new IllegalCommandException("No more gold cards in the deck");
            }
        }
        // if chosen deck is resource then draw from resource deck
        else if (fromDeck.equals("RESOURCE")) {
            try {
                card = super.game.getDeck().drawResourceCard();
            } catch (EmptyStackException e) {
                throw new IllegalCommandException("No more resource cards in the deck");
            }
        }
        // if chosen card is from uncovered cards then draw from uncovered cards
        else {
            // check what card in the uncovered cards has the same id as the chosen card
            super.game.getBoard().removeUncoveredCard(card);

            // draw a new card from the same deck as the chosen card and add it to the
            // uncovered cards
            if (card instanceof GoldCard) {
                super.game.getBoard().addUncoveredCard(super.game.getDeck().drawGoldCard());
            } else if (card instanceof ResourceCard) {
                super.game.getBoard().addUncoveredCard(super.game.getDeck().drawResourceCard());
            }
        }
        // add card in the hand of the player
        super.game.getHandByPlayer(super.game.getCurrentPlayer()).addCard(card);

    }

    /**
     * Advances the game to the next turn.
     * 
     * @return {@code true} if the match has ended after the current turn, otherwise
     *         {@code false}.
     */
    private Boolean nextTurn() {
        List<Player> players = super.game.getPlayers();
        boolean matchEnded = false;
        super.game.setCurrentPlayer(players.get((players.indexOf(super.game.getCurrentPlayer())
                + 1) % players.size()));

        if (super.game.isLastTurn()) {
            if (super.game.getTurnCounter() > 0) {
                System.out.println("turn counter before remove " + super.game.getTurnCounter());
                super.game.removeTurn();
                System.out.println("turn counter after remove " + super.game.getTurnCounter());
            } else {
                System.out.println("end game state");
                matchEnded = true;
            }
        }

        if (!super.game.getConnected().get(super.game.getCurrentPlayer()))
            if (!matchEnded)
                matchEnded = nextTurn();

        return matchEnded;
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

        Event event = null;
        boolean foundNickname = false;

        // Checks if the nickname is correct
        // Checks if the player results disconnected
        // Checks if the password is correct
        // Hence proceedes to send all the information about the game to the rejoined
        // client with a RejoinGameEvent
        for (var player : game.getPlayers()) {
            if (nickname.equals(player.getNickname())) {
                foundNickname = true;
                if (password.equals(player.getPassword())) {
                    if (!game.getConnected().get(player)) {
                        game.getConnected().put(player, true);
                        super.game.getFromPlayerToId().put(player, clientId);
                        Player tempPlayer = null;
                        Event event1 = null;
                        for (Player p : game.getFromPlayerToId().keySet()) {
                            if (game.getFromPlayerToId().get(p).equals(clientId)) {
                                tempPlayer = p;
                                break;
                            }
                        }
                        if (tempPlayer != null) {
                            event1 = new ErrorEvent(null, game.getGameId(),
                                    tempPlayer.getNickname() + " has rejoined");
                            super.rmiServer.sendEvent(event1);
                            try {
                                super.socketServer.sendEvent(event1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        event = new RejoinGameEvent(clientId, nickname, game.getGameId(), "Draw", game.getPlayers(),
                                game.getStructures(),
                                game.getHands(), game.getBoard(), game.getDeck(), game.getCurrentPlayer(), null);
                        System.out.println("trying to reconnect client");
                        System.out.println(clientId + " " + nickname + " " + game.getGameId() + " " + "Place" + " "
                                + game.getPlayers() + " " + game.getStructures() + " " +
                                game.getHands() + " " + game.getBoard() + " " + game.getDeck() + " "
                                + game.getCurrentPlayer());
                    } else {
                        event = new ErrorEvent(clientId, game.getGameId(), "the player is already connected");
                    }
                } else {
                    event = new ErrorEvent(clientId, game.getGameId(), "wrong password");
                }
                break;
            }
        }

        if (!foundNickname) {
            event = new ErrorEvent(clientId, game.getGameId(),
                    "no player with this nickname in the game " + game.getGameId());
        }

        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Handles disconnection of a client from the game.
     * 
     * @param clientId The client identifier.
     */
    @Override
    public synchronized void disconnect(String clientId) {
        boolean keepOn = true;
        Player player = game.PlayerFromId(clientId);
        super.game.getConnected().put(player, false);

        // cheks if there is only one player left
        // in this case waits for 30 seconds before shutting down the game
        // if a player reconnects proceeds to continue with the game
        if (game.onePlayerLeft()) {
            Event event = new ErrorEvent(null, game.getGameId(),
                    "You are the only player left, waiting for others to rejoin the game...");
            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }

            for (int i = 0; i < 30; i++) {
                try {
                    System.out.println("waiting for the client ot come back");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!game.onePlayerLeft())
                    break;
            }
            System.out.println("onePlayerLeft returns: " + game.onePlayerLeft());
            if (game.onePlayerLeft()) {
                keepOn = false;
                event = new ForcedEndEvent(game.getGameId(), "Game was shut down due to clients' disconnections");
                super.rmiServer.sendEvent(event);
                try {
                    super.socketServer.sendEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                super.game.setState(new ForcedEndState(super.game, super.rmiServer, super.socketServer));
            }
        } else {
            Player tempPlayer = null;
            Event event = null;
            for (Player p : game.getFromPlayerToId().keySet()) {
                if (game.getFromPlayerToId().get(p).equals(clientId)) {
                    tempPlayer = p;
                    break;
                }
            }
            if (tempPlayer != null && !tempPlayer.equals((game.getCurrentPlayer()))) {
                event = new ErrorEvent(null, game.getGameId(),
                        tempPlayer.getNickname() + " has disconnected");
                super.rmiServer.sendEvent(event);
                try {
                    super.socketServer.sendEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (keepOn && player.equals((game.getCurrentPlayer()))) {
            System.out.println("value of drawn = " + drawn);
            if (!drawn) { // should always get in
                System.out.println("restoring the structure and hand pre disconnection");
                super.game.revert();
            }

            boolean matchEnded = nextTurn();

            Event event = new PlaceEvent(game.getGameId(), "Place", game.getStructures(), game.getHands(),
                    game.getBoard());

            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }

            event = new DrawEvent(game.getGameId(), "Place", game.getHands(), game.getCurrentPlayer(),
                    game.getDeck(),
                    game.getBoard(), game.getTurnCounter(), game.isLastTurn());

            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (matchEnded)
                super.game.setState(new EndGameState(super.game, super.rmiServer, super.socketServer));
            else
                super.game.setState(new PlacedCardState(super.game, super.rmiServer, super.socketServer));
        }
    }

    /**
     * Skips the turn if the client has disconnected
     */
    public void skipTurn() {

        if (game.getSkip()) {
            System.out.println("skipping the turn");
            game.setSkip(false);
            System.out.println(game.getSkip());

            boolean matchEnded = nextTurn();

            Event event = new PlaceEvent(game.getGameId(), "Place", game.getStructures(), game.getHands(),
                    game.getBoard());

            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }

            event = new DrawEvent(game.getGameId(), "Place", game.getHands(), game.getCurrentPlayer(), game.getDeck(),
                    game.getBoard(), game.getTurnCounter(), game.isLastTurn());

            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (matchEnded)
                super.game.setState(new EndGameState(super.game, super.rmiServer, super.socketServer));
            else {
                System.out.println(super.game.getState().getClass());
                super.game.setState(new PlacedCardState(super.game, super.rmiServer, super.socketServer));
                System.out.println(super.game.getState().getClass());
            }

        }
    }

}