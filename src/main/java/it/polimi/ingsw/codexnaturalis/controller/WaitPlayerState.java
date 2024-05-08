package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.JoinGameEvent;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

public class WaitPlayerState extends ControllerState {
    public WaitPlayerState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        super(game, rmiServer, socketServer);
    }

    @Override
    public void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException {
        throw new IllegalCommandException("Game already initialized");
    }

    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
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

        createNewPlayers(nickname, color);
    }

    @Override
    public void chooseSetUp(Player player, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {
        throw new IllegalCommandException("Game not set up yet");
    }

    private void createNewPlayers(String nickname, Color color) {
        Player player = super.game.getPlayers().get(super.game.getNumParticipants());
        player.setNickname(nickname);
        player.setColor(color);
        super.game.addParticipant();

        // FIXME: clean this up
        try {
            super.game.getBoard().updateActualScore(player, 0);
            // FIXME: this is a temporary solution
            super.game.getBoard().updateActualScore(player, 19);
        } catch (IllegalCommandException e) {
            e.printStackTrace();
        }

        if (isFull()) {

            Event event = new JoinGameEvent("Choose", game.getPlayers(), game.getStructures(), game.getHands(),
                    game.getBoard(), game.getDeck(), game.getCurrentPlayer(), null);
            super.rmiServer.sendEvent(event);
            try {
                super.socketServer.sendEvent(event);
            } catch (Exception e) {
                e.printStackTrace();
            }

            super.game.setState(new ChooseSetUpState(super.game, super.rmiServer, super.socketServer, null));
        } else {

            Event event = new JoinGameEvent("Wait", game.getPlayers(), game.getStructures(), game.getHands(),
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

}