package it.polimi.ingsw.codexnaturalis.controller;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteChair;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOR;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteStair;
import it.polimi.ingsw.codexnaturalis.network.events.EndGameEvent;
import it.polimi.ingsw.codexnaturalis.network.events.ErrorEvent;
import it.polimi.ingsw.codexnaturalis.network.events.Event;
import it.polimi.ingsw.codexnaturalis.network.server.RmiServer;
import it.polimi.ingsw.codexnaturalis.network.server.SocketServer;
import javafx.util.Pair;

/**
 * Represents the state of the game when it has ended.
 * This state handles the necessary finalization tasks such as calculating the
 * points gathered from the objectives and declaring the
 * winner.
 */
public class EndGameState extends ControllerState {

    /**
     * Constructs an EndGameState with the specified game, RMI server, and socket
     * server.
     * This constructor calls methods to finalize the match and declare the winner.
     *
     * @param game         The game instance that this state is associated with.
     * @param rmiServer    The RMI server used for remote method invocation.
     * @param socketServer The socket server used for socket communication.
     */
    public EndGameState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        super(game, rmiServer, socketServer);
        matchEnded();
        declareWinner();
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
        String error = "Match Ended";
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
        String error = "Match Ended";
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
        String error = "Match Ended";
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
        String error = "Match Ended";
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
     * Sends an error event indicating that the player cannot draw a card at the
     * current moment.
     *
     * @param clientId The client ID attempting to place the card.
     * @param player   The player attempting to place the card.
     * @param card     The card to draw
     * @param fromDeck The deck to draw from
     */
    @Override
    public void drawnCard(String clientId, Player player, Card card, String fromDeck) {
        String error = "Match Ended";
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
     * Handles disconnection of a client.
     *
     * @param clientId The client identifier.
     */
    @Override
    public void disconnect(String clientId) {
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
        String error = "Match Ended";
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
     * This method's aim is to gather in a single data structure the pattern
     * objective that a certain player aspire to verify
     *
     * @param player the player I'm interested in
     * @return the list of pattern objective cards available on the board and in the
     * specific player's hand
     */
    private List<Card> gatherPatterns(Player player) {
        List<Card> totPatterns = new ArrayList<>();

        Card secretObjCard = super.game.getHandByPlayer(player).getSecretObjective();
        String idSecretObj = secretObjCard.getIdCard();

        if (idSecretObj.startsWith("OP")) {
            totPatterns.add(secretObjCard);
        }

        Card commonObj0 = super.game.getBoard().getCommonObjectives().getFirst();
        String idCommonObj0 = commonObj0.getIdCard();
        if (idCommonObj0.startsWith("OP")) {
            totPatterns.add(commonObj0);
        }
        Card commonObj1 = super.game.getBoard().getCommonObjectives().get(1);
        String idCommonObj1 = commonObj1.getIdCard();
        if (idCommonObj1.startsWith("OP")) {
            totPatterns.add(commonObj1);
        }

        return totPatterns;
    }

    /**
     * This method's aim is to gather in a single data structure the totem
     * objective that a certain player aspire to verify
     *
     * @param player the player I'm interested in
     * @return the list of totem pattern cards available on the board and in the
     * specific player's hand
     */
    private List<Card> gatherTotem(Player player) {
        List<Card> totPatterns = new ArrayList<>();

        Card secretObjCard = super.game.getHandByPlayer(player).getSecretObjective();
        String idSecretObj = secretObjCard.getIdCard();

        if (idSecretObj.startsWith("OR")) {
            totPatterns.add(secretObjCard);
        }

        Card commonObj0 = super.game.getBoard().getCommonObjectives().getFirst();
        String idCommonObj0 = commonObj0.getIdCard();
        if (idCommonObj0.startsWith("OR")) {
            totPatterns.add(commonObj0);
        }
        Card commonObj1 = super.game.getBoard().getCommonObjectives().get(1);
        String idCommonObj1 = commonObj1.getIdCard();
        if (idCommonObj1.startsWith("OR")) {
            totPatterns.add(commonObj1);
        }

        return totPatterns;
    }

    /**
     * This method gathers togheter all the objective cards (common and secret) in a
     * proper data structure.
     * Then it fills the strategyMap game's attribute with the proper pair
     * (strategy, objective card).
     * This map will be passed to the game method which computes the total amount of
     * virtual points for each player. Note that only 3 possible concrete strategy
     * could be key of the pair:
     * ObjectiveResource strategy, ObjectiveChairPattern, ObjectiveStairPattern.
     */

    private void setStrategies(Player player) throws IllegalCommandException {
        super.game.getStrategyMap().put(player, new ArrayList<>());
        // if (super.game.getStrategyMap().get(player) != null) {
        // super.game.getStrategyMap().get(player).clear();
        // }

        List<Card> gatheredObj = gatherPatterns(player);
        gatheredObj.addAll(gatherTotem(player));

        for (Card card : gatheredObj) {
            if (card.getIdCard().startsWith("OR")) {
                super.game.getStrategyMap().get(player).add(new Pair<>(new ConcreteOR(), card));
            } else if (card.getIdCard().startsWith("OP")) {
                if (card.getShape().equals("STAIRS"))
                    super.game.getStrategyMap().get(player).add(new Pair<>(new ConcreteStair(), card));
                else if (card.getShape().equals("CHAIR"))
                    super.game.getStrategyMap().get(player).add(new Pair<>(new ConcreteChair(), card));
                else
                    throw new IllegalCommandException(
                            "Neither a chair nor a stair card passed, but yet recognized as pattern");
            } else {
                throw new IllegalCommandException("Neither a resourceObject nor a pattern card passed");
            }
        }
    }

    /**
     * This method iterates on players and for each of them calls the method which
     * fills the strategy map
     * with concrete strategies and update his
     * virtual points by calling the getPatternsTotemPoints game's method which is
     * going to perform the search in the
     * reduced matrix. Then the virtual points returned from the game are summed to
     * the actual ones.
     */
    private void matchEnded() {
        try {
            int virtualPoints = 0;
            for (Player player : super.game.getPlayers()) {
                try {
                    // fill player's strategy map with the pair <Strategy, ObjectiveCard>.
                    // Structure of the map: <Player,List<Pair<Strategy, ObjectiveCard>>

                    setStrategies(player);

                    // for each player compute the points made from patterns and from totems

                    virtualPoints += super.game.getPatternsTotemPoints(player, super.game.getStrategyMap());

                    // virtual points becomes actual
                    super.game.getBoard().updateActualScore(player, virtualPoints);
                    virtualPoints = 0;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            super.game.throwException(e.getMessage());
        }
    }

    /**
     * This class looks at each player's actual score and declare the winner,
     * if there is a tie, the winner is the one with the most objective cards. If
     * the tie remains there are multiple winners.
     */
    private void declareWinner() {
        Integer max = 0;
        List<Player> currentWinner = new ArrayList<>();

        for (Player player : super.game.getPlayers()) {
            Integer pointsByPlayerX = super.game.getBoard().getActualPoints(player);

            if (currentWinner.isEmpty()) {
                currentWinner.add(player);
                max = pointsByPlayerX;
            } else if (pointsByPlayerX > max) {
                max = pointsByPlayerX;
                currentWinner.clear();
                currentWinner.add(player);
            } else if (pointsByPlayerX.equals(max)) {
                if (super.game.getStructureByPlayer(player).getSatisfiedObj() > super.game
                        .getStructureByPlayer(currentWinner.getFirst()).getSatisfiedObj()) {
                    currentWinner.clear();
                    currentWinner.add(player);
                } else if (super.game.getStructureByPlayer(player).getSatisfiedObj() == super.game
                        .getStructureByPlayer(currentWinner.getFirst()).getSatisfiedObj()) {
                    currentWinner.add(player);
                }
            }
        }

        if (currentWinner.size() == 1) {
            System.out.println("The winner is: " + currentWinner.getFirst().getNickname());
            super.game.pushEvent("The winner is: " + currentWinner.getFirst().getNickname());
        } else if (currentWinner.size() > 1) {
            List<Player> gravyTrain = new ArrayList<>();
            System.out.println("The winners are: ");
            for (Player cowinner : currentWinner) {
                gravyTrain.add(cowinner);
                System.out.println(" " + cowinner.getNickname());
            }

            StringBuilder message = new StringBuilder("The winners are: ");
            for (Player player : gravyTrain) {
                message.append(player.getNickname());
                message.append(" ");
            }
            super.game.pushEvent(message.toString());
        }

        System.out.println("Final scores: " + super.game.getBoard().getActualScores());

        Event event = new EndGameEvent(game.getGameId(), "End", game.getBoard(), currentWinner);
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
