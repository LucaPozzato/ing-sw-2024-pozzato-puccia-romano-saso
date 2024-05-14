package it.polimi.ingsw.codexnaturalis.controller;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.client.RmiClient;
import it.polimi.ingsw.codexnaturalis.network.events.ErrorEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.events.PlaceEvent;
import it.polimi.ingsw.codexnaturalis.network.events.RejoinGameEvent;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import javafx.util.Pair;

public class PlacedCardState extends ControllerState {
    // TODO: remove regionMatches and use equals -> code more readable
    private boolean placed;

    public PlacedCardState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        super(game, rmiServer, socketServer);
        placed = false;
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
            game.setBackUpStructure(structure);
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

        } catch (IllegalCommandException e) {
            event = new ErrorEvent(clientId, game.getGameId(), e.getMessage());
        }

        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*
         * TODO:manca totalmente la corrispondenza tra player e il client per sapere se
         * è ancora connesso
         * if(rmiServer.getConnected().get(player).equals("false")){
         * structure.removeFromStructure(father, placeThis)
         * addToHand(placeThis)
         * resetActualPoints(pointFromCard)
         * super.game.setState(new PlaceCardState(super.game, super.rmiServer,
         * super.socketServer));
         * }else{
         */
        super.game.setState(new DrawnCardState(super.game, super.rmiServer, super.socketServer));
        // Points resulting from resources objective are computed at the end of the game
        // (END GAME STATE) since they could be covered anytime during the match

        placed = true;
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

    private void removeFromHand(Card placeThis) throws IllegalCommandException {
        // Card bottomCard;
        Hand hand = super.game.getHandByPlayer(super.game.getCurrentPlayer());
        game.setBackUpHand(hand);
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


    @Override
    public void rejoinGame (String clientId, String nickname, String password) {

        Event event = null;

        boolean foundNickname = false;
        for (var player : game.getPlayers()) {
            if (nickname.equals(player.getNickname())) {
                foundNickname = true;
                if (password.equals(player.getPassword())) {
                    if (!game.getConnected().get(player)) {
                        game.getConnected().put(player, true);
                        super.game.getFromPlayerToId().put(player, clientId);
                        event = new RejoinGameEvent(clientId, nickname, game.getGameId(), "Place", game.getPlayers(), game.getStructures(),
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


//            //siamo nella partita
//            String oldClientId = (games.get(gameId).ClientIdFromNickname(command.getNickName()));
//            //abbiamo il vecchio clientId
//            RmiClient oldClient = null;
//            for (var c1 : clients)
//                if (c1.getClientId().equals(oldClientId))
//                    oldClient = c1;
//            //abbiamo il vecchio client, forse ora non serve, dopo si
//
//            //vogliamo controllare che il nickname del comando (command.getNickName)
//
//
//            if (oldClientId.equals(command.getClientId()))
//                for (VirtualClient cl : clients) {
//                    if (cl.getClientId = clId)
//
//                        for (String clId : players.get(gameId)) {
//                            String existingClientId = (games.get(gameId).ClientIdFromNickname(command.getNickName()));
//                            //controlliamo che il nickname nel comando sia tra i nickanme dei plaeyrs in partita
//                            RmiClient client = null;
//                            if (existingClientId.equals(command.getClientId()))
//                                for (var cl : clients) {
//                                    if (cl.getClientId = clId)
//
//                                }
//
//                            if (command.getPassword().equals(getPassword()))
//                        }
//                }
//        }

        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public synchronized void disconnect(String clientId) {
        Player player = game.PlayerFromId(clientId);
        super.game.getConnected().put(game.PlayerFromId(clientId), false);

        if(game.onePlayerLeft())
            //timeout + check again
            super.game.setState(new EndGameState(super.game, super.rmiServer, super.socketServer));
        else if (player.equals((game.getCurrentPlayer()))) {
            if (placed) { //dovrebbe non entrare mai
                game.getStructures().set(game.getPlayers().indexOf(player), game.getBackUpStructure());
                game.getHands().set(game.getPlayers().indexOf(player), game.getBackUpHand());
            }
            game.setSkip(true);
            super.game.setState(new DrawnCardState(super.game, super.rmiServer, super.socketServer));
        }
    }
}