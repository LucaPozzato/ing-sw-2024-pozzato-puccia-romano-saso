package it.polimi.ingsw.codexnaturalis.controller;

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
import it.polimi.ingsw.codexnaturalis.network.events.PlaceEvent;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import javafx.util.Pair;

public class PlacedCardState extends ControllerState {
    // TODO: remove regionMatches and use equals -> code more readable
    public PlacedCardState(Game game, RmiServer rmiServer, SocketServer socketServer) {
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
    public void joinGame(String clientId, String nickname, Color color)  {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Game already joined");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

    @Override
    public void placedCard(String clientId, Player player, Card father, Card placeThis, String position, Boolean frontUp) {

        Event event = null;

        try {
            for (Player p : super.game.getPlayers()) {
                if (p.getNickname().equals(player.getNickname())) {
                    player = p;
                    break;
                }
            }

            if (!player.equals(game.getCurrentPlayer()))
                throw new IllegalCommandException("Not your turn");

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
                System.out.println("before setting last turn");
                game.setLastTurn();
                System.out.println("after setting last turn: " + game.isLastTurn());
            }

            event = new PlaceEvent(game.getGameId(), "Draw", game.getStructures(), game.getHands(), game.getBoard());

        } catch (IllegalCommandException e){
            event = new ErrorEvent(clientId, game.getGameId(), e.getMessage());
        }

        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        super.game.setState(new DrawnCardState(super.game, super.rmiServer, super.socketServer));
        // Points resulting from resources objective are computed at the end of the game
        // (END GAME STATE) since they could be covered anytime during the match
    }

    @Override
    public void drawnCard(String clientId, Player player, Card card, String fromDeck) {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Cannot draw a card now");
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

    private void removeFromHand(Card placeThis) throws IllegalCommandException {
        // Card bottomCard;
        Hand hand = super.game.getHandByPlayer(super.game.getCurrentPlayer());
        // iterate over list of cards in the hand of the player to find the card with
        // the same id and then add it to the structure and remove it from the hand
        for (Card card : hand.getCardsHand()) {
            if (card.equals(placeThis)) {
                // bottomCard = structure.getCard(idBottomCard);
                // structure.insertCard(bottomCard, card, position);
                hand.removeCard(card);
                break;
            }
        }
    }

    private void updateActualPoints(int points) throws IllegalCommandException {
        super.game.getBoard().updateActualScore(super.game.getCurrentPlayer(), points);
    }
}