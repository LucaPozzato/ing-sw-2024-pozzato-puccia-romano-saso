package it.polimi.ingsw.codexnaturalis.controller;

import java.util.EmptyStackException;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.chat.ChatMessage;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.events.DrawEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.PlaceEvent;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

public class DrawnCardState extends ControllerState {
    public DrawnCardState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        super(game, rmiServer, socketServer);
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
    public void chooseSetUp(Player nickname, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {
        throw new IllegalCommandException("Game already set up");
    }

    @Override
    public void placedCard(Player player, Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException {
        throw new IllegalCommandException("Card already placed");
    }

    @Override
    public void drawnCard(Player player, Card card, String fromDeck) throws IllegalCommandException {
        if (!player.equals(super.game.getCurrentPlayer())) {
            throw new IllegalCommandException("Not your turn");
        }

        updateDeck(card, fromDeck);
        nextTurn();

        Event event = new DrawEvent("Place", game.getHands(), game.getBoard(), game.getTurnCounter(), game.isLastTurn());
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.game.setState(new PlacedCardState(super.game, super.rmiServer, super.socketServer));
    }

//    @Override
//    public abstract void text(String message, Player sender, Player receiver/*, long timeStamp*/) throws IllegalCommandException {
//        ChatMessage chatMessage = new ChatMessage(message, sender, receiver, 0);
//        //right know the chat is not part of the game hp:we instantiate it in the contruction of the game and keep an attribute of it
//        super.game.getChat().addMessage(chatMessage);
//    }


    private void updateDeck(Card card, String fromDeck) throws IllegalCommandException {
        Card tempCard = null;

        if (super.game.getDeck().getGoldDeck().isEmpty() && super.game.getDeck().getResourceDeck().isEmpty()) {
            super.game.setLastTurn();
        }

        // if chosen deck is gold then draw from gold deck
        if (fromDeck.equals("GOLD")) {
            try {
                tempCard = super.game.getDeck().drawGoldCard();
            } catch (EmptyStackException e) {
                throw new IllegalCommandException("No more gold cards in the deck");
            }
        }
        // if chosen deck is resource then draw from resource deck
        else if (fromDeck.equals("RESOURCE")) {
            try {
                tempCard = super.game.getDeck().drawResourceCard();
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
                tempCard = super.game.getDeck().drawGoldCard();
                super.game.getBoard().addUncoveredCard(tempCard);
            } else if (card instanceof ResourceCard) {
                tempCard = super.game.getDeck().drawResourceCard();
                super.game.getBoard().addUncoveredCard(tempCard);
            }
        }
        // add card in the hand of the player
        super.game.getHandByPlayer(super.game.getCurrentPlayer()).addCard(tempCard);

    }

    private void nextTurn() {
        super.game.setCurrentPlayer(super.game.getNextPlayer());
        if (super.game.isLastTurn()) {
            if (super.game.getTurnCounter() >= 0)
                // [ ] test the >= 0
                super.game.removeTurn();
            else
                super.game.setState(new EndGameState(super.game, super.rmiServer, super.socketServer));
        }
        List<Player> players = super.game.getPlayers();
        super.game.setNextPlayer(players.get((players.indexOf(super.game.getCurrentPlayer()) + 1) % players.size()));
    }
}