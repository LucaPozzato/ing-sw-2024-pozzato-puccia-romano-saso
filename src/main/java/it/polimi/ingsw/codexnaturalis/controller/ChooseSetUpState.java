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
import it.polimi.ingsw.codexnaturalis.network.events.StartGameEvent;
import it.polimi.ingsw.codexnaturalis.network.events.WaitEvent;
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
    public void initialized(String clientId, String nick, String password, Color color, int numPlayers) {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Game already initialized");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public void chooseSetUp(String clientId, Player player, Boolean side, ObjectiveCard objCard) {

        Event event = null;

        try {

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

                event = new StartGameEvent(game.getGameId(), "Place", game.getPlayers(), game.getStructures(),
                        game.getHands(), game.getBoard(), game.getDeck(), game.getCurrentPlayer(), null);

            } else {
                // for the interface to be responsive, we might want to send an event for each
                // choice. This way the player doesn't need
                // to wait for all the choices to be made before being able to see his
                event = new WaitEvent(clientId, super.game.getGameId(), "Wait");

                super.game.setState(new ChooseSetUpState(super.game, super.rmiServer, super.socketServer, setUpMap));
            }

        } catch (IllegalCommandException err) {
            event = new ErrorEvent(clientId, game.getGameId(), err.getMessage());
        }

        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void disconnect(String clientId){
        //terminare la partita
        super.game.setState(new EndGameState(super.game, super.rmiServer, super.socketServer));
    }

    @Override
    public void rejoinGame (String clientId, String nickname, String password) {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Can't rejoin game now");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}