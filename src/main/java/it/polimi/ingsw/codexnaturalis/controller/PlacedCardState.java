package it.polimi.ingsw.codexnaturalis.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.events.ErrorEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.ForcedEndEvent;
import it.polimi.ingsw.codexnaturalis.network.events.PlaceEvent;
import it.polimi.ingsw.codexnaturalis.network.events.RejoinGameEvent;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import javafx.util.Pair;

/**
 * Represents the state of the game where a card has been placed by a player.
 */
public class PlacedCardState extends ControllerState {
    private boolean placed;

    /**
     * Constructs a new PlacedCardState.
     *
     * @param game         The game instance.
     * @param rmiServer    The RMI server instance.
     * @param socketServer The socket server instance.
     */
    public PlacedCardState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        super(game, rmiServer, socketServer);

        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);

            oos.writeObject(new Pair<>(super.game.getStructureByPlayer(super.game.getCurrentPlayer()),
                    super.game.getHandByPlayer(super.game.getCurrentPlayer())));

            oos.flush();
            oos.close();
            bos.close();

            byte[] byteData = bos.toByteArray();

            ByteArrayInputStream bais = new ByteArrayInputStream(byteData);
            Pair<Structure, Hand> backUpPair = (Pair<Structure, Hand>) new ObjectInputStream(bais).readObject();
            game.setBackUpStructure(backUpPair.getKey());
            game.setBackUpHand(backUpPair.getValue());

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        placed = false;
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
        Event event = new ErrorEvent(clientId, game.getGameId(), error);
        super.game.pushEvent(error);
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
        String error = "Game already joined";
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
        String error = "Game already set up";
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
     * Handles the placement of a card by a player.
     *
     * @param clientId  The client ID attempting to place the card.
     * @param player    The player attempting to place the card.
     * @param father    The card where the placement is initiated.
     * @param placeThis The card being placed.
     * @param position  The position where the card is placed.
     * @param frontUp   The orientation of the card.
     */
    @Override
    public synchronized void placedCard(String clientId, Player player, Card father, Card placeThis, String position,
                                        Boolean frontUp) {

        Event event = null;

        try {
            for (Player p : super.game.getPlayers()) {
                if (p.getNickname().equals(player.getNickname())) {
                    player = p;
                    break;
                }
            }

            if (!player.equals(game.getCurrentPlayer())) {
                throw new IllegalCommandException("Not your turn");
            }


            // FIXME: throw all necessary exceptions

            if (father.getIdCard().equals(super.game.getHandByPlayer(player).getInitCard().getIdCard()))
                father = super.game.getHandByPlayer(player).getInitCard();
            else {
                for (Pair<Card, Boolean> card : super.game.getStructureByPlayer(player).getPlacedCards()) {
                    if (card.getKey().getIdCard().equals(father.getIdCard())) {
                        father = card.getKey();
                        break;
                    }
                }
            }

            for (Card card : super.game.getHandByPlayer(player).getCardsHand()) {
                if (card.getIdCard().equals(placeThis.getIdCard())) {
                    placeThis = card;
                    break;
                }
            }

            // BUG: exceptions are lost

            Structure structure = super.game.getStructureByPlayer(super.game.getCurrentPlayer());
            structure.placeCard(father, placeThis, position, frontUp);
            removeFromHand(placeThis);

            // Compute the points which are direct consequence of card's placement:
            // Points assigned because the placed card is a gold card or a resource card
            // with bonus points (these are actual points,
            // immediately assigned to the player who placed the card and which determines a
            // movement of his pawn on the board)

            int pointFromCard = structure.getPointsFromPlayableCard(placeThis, frontUp);
            updateActualPoints(pointFromCard);

            if (game.getBoard().getActualPoints(player) >= 20 && !game.isLastTurn()) {
                game.setLastTurn();
            }

            event = new PlaceEvent(game.getGameId(), "Draw", game.getStructures(), game.getHands(), game.getBoard());

            super.game.setState(new DrawnCardState(super.game, super.rmiServer, super.socketServer));

        } catch (IllegalCommandException e) {
            event = new ErrorEvent(clientId, game.getGameId(), e.getMessage());
            super.game.pushEvent(e.getMessage());
        }

        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }

        // Points resulting from resources objective are computed at the end of the game
        // (END GAME STATE) since they could be covered anytime during the match

        placed = true;
    }

    /**
     * Sends an error event indicating that the player cannot draw a card at the
     * current moment.
     *
     * @param clientId The client ID attempting to place the card.
     * @param player   The player attempting to place the card.
     * @param card     The card to draw
     * @param fromDeck The deck from which to draw
     */
    @Override
    public void drawnCard(String clientId, Player player, Card card, String fromDeck) {
        String error = "Error while drawing";
        Event event = null;
        if (!player.equals(super.game.getCurrentPlayer())) {
            super.game.pushEvent(error);
            event = new ErrorEvent(clientId, game.getGameId(), "Not your turn");
        } else {
            super.game.pushEvent(error);
            event = new ErrorEvent(clientId, game.getGameId(), "Can't draw card now");
        }

        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            String noSer = "No server found";
            super.game.pushEvent(noSer);
            e.printStackTrace();
        }
    }

    private void removeFromHand(Card placeThis) throws IllegalCommandException {
        // Card bottomCard;
        Hand hand = super.game.getHandByPlayer(super.game.getCurrentPlayer());
        // Iterate over list of cards in the hand of the player to find the card with
        // the same id and then add it to the structure and remove it from the hand
        for (Card card : hand.getCardsHand()) {
            if (card.equals(placeThis)) {
                hand.removeCard(card);
                break;
            }
        }
    }

    private void updateActualPoints(int points) throws IllegalCommandException {
        super.game.setBackUpPoints(points);
        super.game.getBoard().updateActualScore(super.game.getCurrentPlayer(), points);
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
        // Hence proceeds to send all the information about the game to the rejoined
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

                        event = new RejoinGameEvent(clientId, nickname, game.getGameId(), "Place", game.getPlayers(),
                                game.getStructures(),
                                game.getHands(), game.getBoard(), game.getDeck(), game.getCurrentPlayer(), null);
                        System.out.println("Trying to reconnect client");
                        System.out.println(clientId + " " + nickname + " " + game.getGameId() + " " + "Place" + " "
                                + game.getPlayers() + " " + game.getStructures() + " " +
                                game.getHands() + " " + game.getBoard() + " " + game.getDeck() + " "
                                + game.getCurrentPlayer());
                    } else {
                        String error = "the player is already connected";
                        super.game.pushEvent(error);
                        event = new ErrorEvent(clientId, game.getGameId(), error);
                    }
                } else {
                    String error = "wrong password";
                    super.game.pushEvent(error);
                    event = new ErrorEvent(clientId, game.getGameId(), error);
                }
                break;
            }
        }

        if (!foundNickname) {
            String error = "no player with this nickname in the game " + game.getGameId();
            super.game.pushEvent(error);
            event = new ErrorEvent(clientId, game.getGameId(),
                    error);
        }

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
     * Handles disconnection of a client from the game.
     *
     * @param clientId The client identifier.
     */
    @Override
    public synchronized void disconnect(String clientId) {
        boolean keepOn = true;
        Player player = game.PlayerFromId(clientId);
        super.game.getConnected().put(game.PlayerFromId(clientId), false);
        System.out.println("Disconnect being called");

        //Let's examine the case in which there's only one player connected
        if (game.onePlayerLeft()) {
            String error = "You are the only player left, waiting for others to rejoin the game...";
            super.game.pushEvent(error);
            Event event = new ErrorEvent(null, game.getGameId(),
                    error);
            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                String noSer = "No server found";
                super.game.pushEvent(noSer);
                e.printStackTrace();
            }

            //The system waits for 30 * 1000 millis for the other player reconnection
            for (int i = 0; i < 30; i++) {
                try {
                    System.out.println("Waiting for the client to come back");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (!game.onePlayerLeft())
                    break;
            }
            //If the player is still the only one connected the game is shut down
            if (game.onePlayerLeft()) {
                String error1 = "Game was shut down due to clients' disconnections";
                super.game.pushEvent(error1);
                keepOn = false;
                event = new ForcedEndEvent(game.getGameId(), error1);
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
        } else {
            // If a generic player gets disconnected and there are at least two other players online
            Player tempPlayer = null;
            Event event;
            for (Player p : game.getFromPlayerToId().keySet()) {
                if (game.getFromPlayerToId().get(p).equals(clientId)) {
                    tempPlayer = p;
                    break;
                }
            }
            if (tempPlayer != null) {
                String error = tempPlayer.getNickname() + " has disconnected";
                super.game.pushEvent(error);
                event = new ErrorEvent(null, game.getGameId(), error);
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

        //Manage the case in which the current player gets disconnected and there are at least other two players online, then his turn is skipped and the game goes on
        if (keepOn && player.equals((game.getCurrentPlayer()))) { // currentPlayer has disconnected
            if (placed) {
                super.game.revert();
            }
            game.setSkip(true);
            super.game.setState(new DrawnCardState(super.game, super.rmiServer, super.socketServer));
            ((DrawnCardState) super.game.getState()).skipTurn();
        }
    }
}