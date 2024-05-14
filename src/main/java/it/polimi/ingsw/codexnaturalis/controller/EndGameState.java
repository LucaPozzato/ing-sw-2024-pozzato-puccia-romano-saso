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

public class EndGameState extends ControllerState {

    public EndGameState(Game game, RmiServer rmiServer, SocketServer socketServer) {
        super(game, rmiServer, socketServer);
        //BUG: metodo di terminazione partita che sia valido per tutti gli stati
        matchEnded();
        declareWinner();
    }

    @Override
    public void initialized(String clientId, String nick, String password, Color color, int numPlayers)  {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Match Ended");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void joinGame(String clientId, String nickname, String password, Color color) {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Match Ended");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void chooseSetUp(String clientId, Player nickname, Boolean side, ObjectiveCard objCard) {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Match Ended");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void placedCard(String clientId, Player player, Card father, Card placeThis, String position, Boolean frontUp){
        Event event = new ErrorEvent(clientId, game.getGameId(), "Match Ended");
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void drawnCard(String clientId, Player player, Card card, String fromDeck) {
        Event event = new ErrorEvent(clientId, game.getGameId(), "Match Ended");
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

    /**
     * This method's aim is to gather in a single data structure the pattern
     * objective that a certain player aspire to verify
     * 
     * @param player the player I'm interested in
     * @return the list of pattern objective cards available on the board and in the
     *         specific player's hand
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
     *         specific player's hand
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
            System.out.println("scores before match ended: " + super.game.getBoard().getActualScores());

            System.out.println("MATCH ENDED BITCHHHHHH");
            int virtualPoints = 0;
            for (Player player : super.game.getPlayers()) {
                try {
                    // fill player's strategy map with the pair <Strategy, ObjectiveCard>.
                    // Structure of the map: <Player,List<Pair<Strategy, ObjectiveCard>>

                    setStrategies(player);
                    System.out.println("La strategy map del game per il player" + player.getNickname() + "è: "
                            + super.game.getStrategyMap());

                    // for each player compute the points made from patterns and from totems

                    // BUG: virtualPoints not correct
                    virtualPoints += super.game.getPatternsTotemPoints(player, super.game.getStrategyMap());

                    System.out.println("new points from pattern: " + virtualPoints);
                    // virtual points becomes actual
                    super.game.getBoard().updateActualScore(player, virtualPoints);
                    virtualPoints = 0;

                    System.out.println("scores after update of player: " + player.getNickname()
                            + super.game.getBoard().getActualScores());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            System.out.println("scores after match ended: " + super.game.getBoard().getActualScores());

        } catch (Exception e) {
            super.game.throwException(e.getMessage());
        }
    }

    /**
     * This class looks at each player's actual score and declare the winner,
     * if there is a tie, the winner is the one with the most objective cards. If
     * the tie remains there are multiple winners.
     *
     */
    private void declareWinner() {
        // System.out.println("declared winner");
        Integer max = 0;
        List<Player> currentWinner = new ArrayList<>();

        // System.out.println("scores before declaration: " +
        // super.game.getBoard().getActualScores());

        for (Player player : super.game.getPlayers()) {
            // System.out.println("satisfied patterns of the player: " +
            // player.getNickname() + ", "
            // + super.game.getStructureByPlayer(player).getSatisfiedPatterns());
            // FIXME: satisfiedPatterns is always 0
            Integer pointsByPlayerX = super.game.getBoard().getActualPoints(player);
            if (pointsByPlayerX > max) {
                max = pointsByPlayerX;
                currentWinner.clear();
                currentWinner.add(player);
            } else if (pointsByPlayerX.equals(max)) {
                if (super.game.getStructureByPlayer(player).getSatisfiedPatterns() > super.game
                        .getStructureByPlayer(currentWinner.getFirst()).getSatisfiedPatterns()) {
                    currentWinner.clear();
                    currentWinner.add(player);
                } else if (super.game.getStructureByPlayer(player).getSatisfiedPatterns() == super.game
                        .getStructureByPlayer(currentWinner.getFirst()).getSatisfiedPatterns()) {
                    currentWinner.add(player);
                }
            }
        }

        if (currentWinner.size() == 1) {
            System.out.println("The winner is: " + currentWinner.getFirst().getNickname());
        } else if (currentWinner.size() > 1) {
            System.out.println("The winners are: ");
            for (Player coWinner : currentWinner) {
                System.out.println(" " + coWinner.getNickname());
            }
        } else {
            System.out.println("No winner");
        }

        System.out.println("final scores: " + super.game.getBoard().getActualScores());

        Event event = new EndGameEvent(game.getGameId(), "End", game.getBoard(), currentWinner);
        super.rmiServer.sendEvent(event);
        try {
            super.socketServer.sendEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
