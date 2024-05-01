package it.polimi.ingsw.codexnaturalis.model.game.strategies;

import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Triplet;

public class ConcreteStair implements Strategy {

    /**
     * The method uses the cardMatrix and the wholeCells card attribute in order to identify the 3 cards which compose the stair pattern in a 3x3 matrix. <b>
     * Than the reduced matrix computed using radius is visited by letting scroll the 3x3 matrix with the highlighted cells on it and counting the matches in patternFound.
     * @param structure the player structure to get the cardmatrix and the cardToCoordinates
     * @param objCard the stair objective we want to look for on the player structure
     * @return the number of points made due to objective satisfaction
     * @throws IllegalCommandException
     */
    @Override
    public int compute(Structure structure, Card objCard) throws IllegalCommandException {
        Map<Card, Triplet<Integer, Boolean, Boolean>> cardToCoordinate = structure.getCardToCoordinate();
        Card[][] matrix = structure.getCardMatrix();
        int radius = structure.getRadius(structure.getCoordinateToCard());
        int[] wholeCells = objCard.getWholeCells();


        int patternsFound = 0;
        for (int i = 40 + radius; i >= 40 - radius + 2; i--) {
            for (int j = 40 - radius; j <= 40 + radius - 2; j++) {

                Card upper = matrix[j + wholeCells[0] % 3][i];
                Card middle = matrix[j + wholeCells[1] % 3][i - 1];
                Card lower = matrix[j + wholeCells[2] % 3][i - 2];

                if ((upper instanceof ResourceCard || upper instanceof GoldCard)
                        && upper.getSymbol().equals(objCard.getMustHave())
                        && !cardToCoordinate.get(upper).getVisited() &&
                        (middle instanceof ResourceCard || middle instanceof GoldCard)
                        && middle.getSymbol().equals(objCard.getMustHave())
                        && !cardToCoordinate.get(middle).getVisited()
                        && (lower instanceof ResourceCard || lower instanceof GoldCard)
                        && lower.getSymbol().equals(objCard.getMustHave())
                        && !cardToCoordinate.get(lower).getVisited()) {

                    cardToCoordinate.get(upper).setVisited(true);
                    cardToCoordinate.get(middle).setVisited(true);
                    cardToCoordinate.get(lower).setVisited(true);
                    patternsFound++;
                }
            }
        }

        return objCard.getPoints() * patternsFound;
    }
}