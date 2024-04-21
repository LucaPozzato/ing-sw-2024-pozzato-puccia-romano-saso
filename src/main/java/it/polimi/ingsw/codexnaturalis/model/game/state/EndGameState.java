package it.polimi.ingsw.codexnaturalis.model.game.state;

import java.util.ArrayList;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Color;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Game;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class EndGameState extends State {

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

    private void matchEnded() {
        try {
            List<Card> objCards = super.game.getBoard().getCommonObjectives();
            Structure structure = null;
            Integer points = 0;

            for (Player player : super.game.getPlayers()) {
                objCards.add(super.game.getHandByPlayer(player).getSecretObjective());
                structure = super.game.getStructureByPlayer(player);
                for (Card card : objCards) {
                    points += structure.getPointsFromObjResCard((ObjectiveCard) card);
                }
                super.game.getBoard().updateVirtualScore(player, points);
                points = 0;
                objCards.removeLast();
            }
        } catch (Exception e) {
            super.game.throwException(e.getMessage());
        }
    }

    private void declareWinner() {
        Integer max = 0;
        List<Player> currentWinner = new ArrayList<>();
        for (Player player : super.game.getPlayers()) {
            if (game.getBoard().getVirtualPoints(player) > max) {
                max = game.getBoard().getVirtualPoints(player);
                currentWinner.clear();
                currentWinner.add(player);
            } else if (game.getBoard().getVirtualPoints(player) == max) {
                // TODO: add a player attribute for the amount of ObjectieCards realized
                // if ( più obiettivi realizzati ){
                // currentWinner.clear();
                // currentWinner.add(player);
                // }
                // else if ( uguali ){
                // currentWinner.add(player);
                // }
                // }
                if (currentWinner.size() == 1) {
                    System.out.println("The winner is: " + currentWinner.get(0).getNickname());
                } else if (currentWinner.size() > 1) {
                    System.out.println("The winners are: ");
                    for (int i = 0; i < currentWinner.size(); i++) {
                        System.out.println(" " + currentWinner.get(i).getNickname());
                    }
                }
            }
            // TODO: if there is a tie, the winner is the one with the most objective cards
            // satisfied
            // TODO: if there is still a tie, there are winners
        }
    }
}