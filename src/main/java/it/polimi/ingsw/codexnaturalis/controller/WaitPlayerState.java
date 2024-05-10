package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.events.ErrorEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.JoinGameEvent;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

public class WaitPlayerState extends ControllerState {
    public WaitPlayerState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        super(game, rmiServer, socketServer);
    }

    @Override
    public void initialized(String clientId, String nick, Color color, int numPlayers) {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Game already initialized");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void joinGame(String clientId, String nickname, Color color) {
        try {
            if (nickname.equals("")) {
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

            createNewPlayers(clientId, nickname, color);
        } catch (IllegalCommandException err) {
            System.out.println("> sent error: " + err.getMessage());
            Event event = new ErrorEvent(clientId, game.getGameId(), err.getMessage());
            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void chooseSetUp(String clientId, Player player, Boolean side, ObjectiveCard objCard) {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Game not set up yet");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createNewPlayers(String clientId, String nickname, Color color) {
        Player player = super.game.getPlayers().get(super.game.getNumParticipants());
        player.setNickname(nickname);
        player.setColor(color);
        super.game.addParticipant();

        // FIXME: clean this up
        try {
            super.game.getBoard().updateActualScore(player, 0);
            // FIXME: this is a temporary solution
            super.game.getBoard().updateActualScore(player, 12);
        } catch (IllegalCommandException e) {
            e.printStackTrace();
        }

        if (isFull()) {

            Event event = new JoinGameEvent(clientId, game.getGameId(), "Choose", game.getPlayers(),
                    game.getStructures(), game.getHands(), game.getBoard(), game.getDeck(), game.getCurrentPlayer(),
                    null);
            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }

            super.game.setState(new ChooseSetUpState(super.game, super.rmiServer, super.socketServer, null));
        } else {
            Event event = new JoinGameEvent(clientId, game.getGameId(), "Wait", game.getPlayers(), game.getStructures(),
                    game.getHands(),
                    game.getBoard(), game.getDeck(), game.getCurrentPlayer(), null);
            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }

            super.game.setState(new WaitPlayerState(super.game, super.rmiServer, super.socketServer));
        }
    }

    private boolean isFull() {
        if (super.game.getPlayers().size() == super.game.getNumParticipants()) {
            return true;
        }
        return false;
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
        Event event = new ErrorEvent(clientId, game.getGameId(), "Can't draw card yet");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // @Override
    // public abstract void text(String message, Player sender, Player receiver/*,
    // long timeStamp*/) throws IllegalCommandException {
    // ChatMessage chatMessage = new ChatMessage(message, sender, receiver, 0);
    // //right know the chat is not part of the game hp:we instantiate it in the
    // contruction of the game and keep an attribute of it
    // super.game.getChat().addMessage(chatMessage);
    // }

}