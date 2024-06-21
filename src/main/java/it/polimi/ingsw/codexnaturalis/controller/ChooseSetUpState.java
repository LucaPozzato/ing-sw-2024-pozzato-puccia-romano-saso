package it.polimi.ingsw.codexnaturalis.controller;

import java.util.HashMap;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.events.ErrorEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.ForcedEndEvent;
import it.polimi.ingsw.codexnaturalis.network.events.StartGameEvent;
import it.polimi.ingsw.codexnaturalis.network.events.WaitEvent;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

/**
 * Controller state representing the phase where players choose their setup
 * options.
 * This state manages the setup choices for each player before the game begins.
 */
public class ChooseSetUpState extends ControllerState {
    Map<Player, Boolean> setUpMap = new HashMap<>();

    /**
     * Constructs a new ChooseSetUpState.
     * 
     * @param game         The current game instance.
     * @param rmiServer    The RMI server handling communication.
     * @param socketServer The Socket server handling communication.
     * @param setUpMap     Map tracking players' setup status.
     */
    public ChooseSetUpState(Game game, RmiServer rmiServer, SocketServer socketServer, Map<Player, Boolean> setUpMap) {
        super(game, rmiServer, socketServer);
        if (setUpMap != null)
            this.setUpMap = setUpMap;
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
        Event event = new ErrorEvent(clientId, game.getGameId(), "Can't place card yet");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sends an error event indicating that the player cannot draw a card at the
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
    public void drawnCard(String clientId, Player player, Card card, String fromDeck) {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Can`t draw card yet");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the event when a player chooses their setup options.
     * 
     * @param clientId The client identifier.
     * @param player   The player making the setup choice.
     * @param side     The chosen side for placement.
     * @param objCard  The chosen objective card.
     */
    @Override
    public void chooseSetUp(String clientId, Player player, Boolean side, ObjectiveCard objCard) {

        Event event = null;

        try {
            // Check if player has already made a choice
            if (setUpMap.keySet().size() > 0 && setUpMap.containsKey(player) && setUpMap.get(player).equals(true)) {
                throw new IllegalCommandException("Player already made his choice");
            } else {
                for (Player p : super.game.getPlayers()) {
                    if (player.getNickname().equals(p.getNickname())) {
                        player = p;
                        break;
                    }
                }
                Hand hand = super.game.getHandByPlayer(player);
                InitialCard initCard = hand.getInitCard();
                super.game.getStructureByPlayer(player).placeCard(null, initCard, null, side);

                for (Card obj : hand.getChooseBetweenObj()) {
                    if (obj.getIdCard().equals(objCard.getIdCard())) {
                        objCard = (ObjectiveCard) obj;
                        break;
                    }
                }

                hand.setSecretObjective(objCard);
                setUpMap.put(player, true);
            }

            // Check if all players have completed setup
            if (setUpMap.keySet().size() == super.game.getNumPlayers()) {
                super.game.setState(new PlacedCardState(super.game, super.rmiServer, super.socketServer));

                event = new StartGameEvent(game.getGameId(), "Place", game.getPlayers(), game.getStructures(),
                        game.getHands(), game.getBoard(), game.getDeck(), game.getCurrentPlayer(), null);

            } else {
                event = new WaitEvent(clientId, super.game.getGameId(), "Wait");

                super.game.setState(new ChooseSetUpState(super.game, super.rmiServer, super.socketServer, setUpMap));
            }

        } catch (IllegalCommandException err) {
            event = new ErrorEvent(clientId, game.getGameId(), err.getMessage());
        }

        // Send event to clients
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
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
        Event event = new ForcedEndEvent(game.getGameId(), "Game was shut down due to clients' disconnections");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
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
        Event event = new ErrorEvent(clientId, game.getGameId(), "Can't rejoin game now");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}