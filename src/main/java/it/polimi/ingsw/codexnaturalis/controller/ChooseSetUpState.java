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
import it.polimi.ingsw.codexnaturalis.network.events.ChooseEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.StartGameEvent;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

public class ChooseSetUpState extends ControllerState {
    Map<Player, Boolean> setUpMap = new HashMap<>();

    public ChooseSetUpState(Game game, RmiServer rmiServer, SocketServer socketServer, Map<Player, Boolean> setUpMap) {
        super(game, rmiServer, socketServer);
        if (setUpMap != null)
            this.setUpMap = setUpMap;
    }

    @Override
    public void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException {
        throw new IllegalCommandException("Game already initialized");
    }

    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        throw new IllegalCommandException("Game already joined");
    }

    @Override
    public void placedCard(Player player, Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException {
        throw new IllegalCommandException("Can't place card yet");
    }

    @Override
    public void drawnCard(Player player, Card card, String fromDeck) throws IllegalCommandException {
        throw new IllegalCommandException("Can't draw card yet");
    }

    // @Override
    // public abstract void text(String message, Player sender, Player receiver/*,
    // long timeStamp*/) throws IllegalCommandException {
    // ChatMessage chatMessage = new ChatMessage(message, sender, receiver, 0);
    // //right know the chat is not part of the game hp:we instantiate it in the
    // contruction of the game and keep an attribute of it
    // super.game.getChat().addMessage(chatMessage);
    // }

    public void chooseSetUp(Player player, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {
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

        if (setUpMap.keySet().size() == super.game.getNumPlayers()) {
            super.game.setState(new PlacedCardState(super.game, super.rmiServer, super.socketServer));

            Event event = new StartGameEvent("Place", game.getPlayers(), game.getStructures(), game.getHands(),
                    game.getBoard(), game.getDeck(), game.getCurrentPlayer(), null);
            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            // for the interface to be responsive, we might want to send an event for each
            // choice. This way the player doesn't need
            // to wait for all the choices to be made before being able to see his
            Event event = new ChooseEvent("Choose", game.getPlayers(), game.getStructures(), game.getHands(),
                    game.getBoard(), game.getDeck(), game.getCurrentPlayer(),
                    null /* is the game.getNextPlayer() already available? */);
            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.game.setState(new ChooseSetUpState(super.game, super.rmiServer, super.socketServer, setUpMap));
        }
    }
}