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
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteStair;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOR;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.Strategy;
import javafx.util.Pair;

public class EndGameState extends ControllerState {

    public EndGameState(Game game) {
        super(game);
        matchEnded();
//        declareWinner();
    }

    @Override
    public void initialized(String nick, Color color, int numPlayers) throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }

    @Override
    public void joinGame(String nickname, Color color) throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }

    @Override
    public void chooseSetUp(Player nickname, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }

    @Override
    public void placedCard(Player player, Card father, Card placeThis, String position, Boolean frontUp)
            throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
    }

    @Override
    public void drawnCard(Player player, Card card, String fromDeck) throws IllegalCommandException {
        throw new IllegalCommandException("Match has ended");
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
     * Then it fills the strategyMap game's attribute with the proper pair (strategy, objective card).
     * This map will be passed to the game method which computes the total amount of
     * virtual points for each player. Note that only 3 possible concrete strategy could be key of the pair:
     * ObjectiveResource strategy, ObjectiveChairPattern, ObjectiveStairPattern.
     */

    private void setStrategies(Player player) throws IllegalCommandException {
         if (super.game.getStrategyMap().get(player) != null) {
         super.game.getStrategyMap().get(player).clear();
         }

        List<Card> gatheredObj = gatherPatterns(player);
        gatheredObj.addAll(gatherTotem(player));

        for (Card card : gatheredObj) {
            if (card.getIdCard().startsWith("OR")) {
                super.game.getStrategyMap().get(player).add(new Pair<>(new ConcreteOR(), card));
            } else if (card.getIdCard().startsWith("OP")) {
                if(card.getShape().equals("STAIRS")) super.game.getStrategyMap().get(player).add(new Pair<>(new ConcreteStair(), card));
                else if(card.getShape().equals("CHAIR")) super.game.getStrategyMap().get(player).add(new Pair<>(new ConcreteChair(), card));
                else throw new IllegalCommandException("Neither a chair nor a stair card passed, but yet recognized as pattern");
            } else {
                throw new IllegalCommandException("Neither a resourceObject nor a pattern card passed");
            }
        }
    }

    /**
     * This method iterates on players and for each of them calls the method which fills the strategy map
     * with concrete strategies and update his
     * virtual points by calling the getPatternsTotemPoints game's method which is going to perform the search in the
     * reduced matrix. Then the virtual points returned from the game are summed to the actual ones.
     */
    private void matchEnded() {
        try {
            int virtualPoints = 0;
            for (Player player : super.game.getPlayers()) {
                // fill player's strategy map with the pair <Strategy, ObjectiveCard>.
                // Structure of the map: <Player,List<Pair<Strategy, ObjectiveCard>>
                setStrategies(player);

                // for each player compute the points made from patterns and from totems
                virtualPoints += super.game.getPatternsTotemPoints(player, super.game.getStrategyMap());
                // virtual points becomes actual
                super.game.getBoard().updateActualScore(player, virtualPoints);

                virtualPoints = 0;
            }
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
        Integer max = 0;
        List<Player> currentWinner = new ArrayList<>();
        for (Player player : super.game.getPlayers()) {
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
            if (currentWinner.size() == 1) {
                System.out.println("The winner is: " + currentWinner.getFirst().getNickname());
            } else if (currentWinner.size() > 1) {
                System.out.println("The winners are: ");
                for (Player coWinner : currentWinner) {
                    System.out.println(" " + coWinner.getNickname());
                }
            }
        }
    }
}
