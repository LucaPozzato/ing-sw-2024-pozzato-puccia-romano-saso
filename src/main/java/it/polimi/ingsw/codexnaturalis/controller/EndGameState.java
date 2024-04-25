package it.polimi.ingsw.codexnaturalis.controller;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOP1;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOP2;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOP3;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOP4;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOP5;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOP6;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOP7;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOP8;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOR1;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOR2;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOR3;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOR4;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOR5;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOR6;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOR7;
import it.polimi.ingsw.codexnaturalis.model.game.strategies.ConcreteOR8;

public class EndGameState extends ControllerState {

    public EndGameState(Game game) {
        super(game);
        matchEnded();
        declareWinner();
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
    public void chooseSetUp(Player nickName, Boolean side, ObjectiveCard objCard) throws IllegalCommandException {
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
     * objective that a certain player could aspire to verify
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
     * This method's aim is to gather in a single data structure the totem objective
     * that a certain player could aspire to verify
     * 
     * @param player the player I'm interested in
     * @return the list of totem objective cards available on the board and in the
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

    // Fin'ora da endGameState viene chiamato il metodo di structure
    // getpointsfromobjrescard.
    // Noi vogliamo astrarci spostando getpointsfromobjrescard in game ovvero
    // ponendo la strategia per risolvere
    // il calcolo dei punti in una strategia concreta.

    /**
     * Builds a list containing at first the patterns and then the totem cards.
     * For each of them the method is going to add a node in the game's strategyMap
     * so that game could perform the computation being agnostic regarding the
     * specific type of strategy to implement.
     * 
     * @param player this map filling operation is made one player at a time so a
     *               single line of the table is actually updated.
     */
    private void setStrategies(Player player) {
        super.game.getStrategyMap().get(player).clear();
        List<Card> gatheredObj = gatherPatterns(player);
        gatheredObj.addAll(gatherTotem(player));

        for (Card card : gatheredObj) {
            switch (card.getIdCard()) {
                case "OP1":
                    super.game.getStrategyMap().get(player).add(new ConcreteOP1());
                case "OP2":
                    super.game.getStrategyMap().get(player).add(new ConcreteOP2());
                case "OP3":
                    super.game.getStrategyMap().get(player).add(new ConcreteOP3());
                case "OP4":
                    super.game.getStrategyMap().get(player).add(new ConcreteOP4());
                case "OP5":
                    super.game.getStrategyMap().get(player).add(new ConcreteOP5());
                case "OP6":
                    super.game.getStrategyMap().get(player).add(new ConcreteOP6());
                case "OP7":
                    super.game.getStrategyMap().get(player).add(new ConcreteOP7());
                case "OP8":
                    super.game.getStrategyMap().get(player).add(new ConcreteOP8());
                case "OR1":
                    super.game.getStrategyMap().get(player).add(new ConcreteOR1());
                case "OR2":
                    super.game.getStrategyMap().get(player).add(new ConcreteOR2());
                case "OR3":
                    super.game.getStrategyMap().get(player).add(new ConcreteOR3());
                case "OR4":
                    super.game.getStrategyMap().get(player).add(new ConcreteOR4());
                case "OR5":
                    super.game.getStrategyMap().get(player).add(new ConcreteOR5());
                case "OR6":
                    super.game.getStrategyMap().get(player).add(new ConcreteOR6());
                case "OR7":
                    super.game.getStrategyMap().get(player).add(new ConcreteOR7());
                case "OR8":
                    super.game.getStrategyMap().get(player).add(new ConcreteOR8());
            }
        }
    }

    /**
     * This method iterates on players and for each of them fills the strategy map
     * with concrete strategies and update his
     * virtual points by calling the getPatternsTotemPoints game's method. This
     * method is going to perform the search in the
     * reduced matrix. Then the virtual points are summed to the virtual ones.
     */
    private void matchEnded() {
        try {
            int virtualPoints = 0;
            for (Player player : super.game.getPlayers()) {
                // fill player's strategy map
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
     * This class updates the actual score from each player with the virtual points
     * computed in MatchEnded and declare the winner,
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
