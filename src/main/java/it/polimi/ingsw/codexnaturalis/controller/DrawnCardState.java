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
import it.polimi.ingsw.codexnaturalis.network.events.*;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;

public class DrawnCardState extends ControllerState {
    boolean drawn = false;

    public DrawnCardState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        super(game, rmiServer, socketServer);
        if(game.getSkip()) {
            game.setSkip(false);
            if (nextTurn())
                super.game.setState(new EndGameState(super.game, super.rmiServer, super.socketServer));
            else
                super.game.setState(new PlacedCardState(super.game, super.rmiServer, super.socketServer));
        }
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
        Event event = new ErrorEvent(clientId, game.getGameId(), "Can't place card now");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

            // FIXME: needs to set the next player
            event = new DrawEvent(game.getGameId(), "Place", game.getHands(), game.getCurrentPlayer(), game.getDeck(),
                    game.getBoard(), game.getTurnCounter(), game.isLastTurn());

            if (matchEnded)
                super.game.setState(new EndGameState(super.game, super.rmiServer, super.socketServer));
            else
                super.game.setState(new PlacedCardState(super.game, super.rmiServer, super.socketServer));

        } catch (IllegalCommandException e) {
            event = new ErrorEvent(clientId, game.getGameId(), e.getMessage());
        }

        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        drawn = true;

    }

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
                if (!super.game.getConnected().get(super.game.getCurrentPlayer()))
                    matchEnded = nextTurn();
            } else {
                System.out.println("end game state");
                matchEnded = true;
            }
        }

        return matchEnded;
    }

    @Override
    public void rejoinGame(String clientId, String nickname, String password) {

        Event event = null;

        boolean foundNickname = false;
        for (var player : game.getPlayers()) {
            if (nickname.equals(player.getNickname())) {
                foundNickname = true;
                if (password.equals(player.getPassword())) {
                    if (!game.getConnected().get(player)) {
                        game.getConnected().put(player, true);
                        super.game.getFromPlayerToId().put(player, clientId);
                        event = new RejoinGameEvent(clientId, nickname, game.getGameId(), "Draw", game.getPlayers(), game.getStructures(),
                                game.getHands(), game.getBoard(), game.getDeck(), game.getCurrentPlayer(), null);
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
            event = new ErrorEvent(clientId, game.getGameId(), "no player with this nickname in the game " + game.getGameId());
        }


//
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void disconnect(String clientId){
        Player player = game.PlayerFromId(clientId);
        super.game.getConnected().put(player, false);

        if(game.onePlayerLeft()){
            //timerrrrrrrrr
            if(game.onePlayerLeft()) {
                Event event = new ForcedEndEvent(game.getGameId(), "Game was shut down due to clients' disconnections");
                super.rmiServer.sendEvent(event);
                try {
                    super.socketServer.sendEvent(event);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (player.equals((game.getCurrentPlayer()))) {
            if (!drawn){ //dovrebbe entrare sempre
                game.getStructures().set(game.getPlayers().indexOf(player), game.getBackUpStructure());
                game.getHands().set(game.getPlayers().indexOf(player), game.getBackUpHand());
            }
            if (nextTurn())
                super.game.setState(new EndGameState(super.game, super.rmiServer, super.socketServer));
            else
                super.game.setState(new PlacedCardState(super.game, super.rmiServer, super.socketServer));
        }
    }
}