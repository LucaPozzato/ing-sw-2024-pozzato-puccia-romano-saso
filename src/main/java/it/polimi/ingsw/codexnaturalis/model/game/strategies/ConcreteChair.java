package it.polimi.ingsw.codexnaturalis.model.game.strategies;

import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Triplet;

public class ConcreteChair implements Strategy {
    /**
     * The method uses the cardMatrix and the wholeCells card attribute in order to
     * identify the 3 cards which compose the chair pattern in a 2x4 matrix.
     * Then the reduced matrix computed using radius is visited by letting scroll
     * the 2x4 matrix with the highlighted cells on it and counting the matches in
     * patternFound.
     *
     * @param structure the player structure to get the cardmatrix and the
     *                  cardToCoordinates
     * @param objCard   the chair objective we want to look for on the player
     *                  structure
     * @return the number of points made due to objective satisfaction
     * @throws IllegalCommandException
     */
    @Override
    public int compute(Structure structure, Card objCard) throws IllegalCommandException {
        Map<Card, Triplet<Integer, Boolean, Boolean>> cardToCoordinate = structure.getCardToCoordinate();
        Card[][] matrix = structure.getCardMatrix();
        int radius = structure.getRadius(structure.getCoordinateToCard());
        int[] wholecells = objCard.getWholeCells();

        String[] decomposedMustHave = objCard.getMustHave().split(", ");
        String[] properColor = new String[3];
        for (int i = 0; i < 3; i++) {
            if (i != objCard.getDivideBy_seatColor()) {
                properColor[i] = decomposedMustHave[0];
            } else {
                properColor[i] = decomposedMustHave[1];
            }
        }

        int patternsFound = 0;
        for (int i = 40 + radius; i >= 40 - radius + 3; i--) {
            for (int j = 40 - radius; j <= 40 + radius - 1; j++) {

                Card upper = matrix[j + wholecells[0]][i];

                Card placedUnder;
                if (wholecells[1] == wholecells[0]) {
                    placedUnder = matrix[j + wholecells[1]][i - 2];
                } else {
                    placedUnder = matrix[j + wholecells[1]][i - 1];
                }

                Card lower = matrix[j + wholecells[2]][i - 3];

                structure.printReducedMatrix(structure.getCardMatrix(), structure.getRadius(structure.getCoordinateToCard()));
                System.out.println("-------------------");


                if ((upper instanceof ResourceCard || upper instanceof GoldCard)
                        && upper.getSymbol().equals(properColor[0])
                        && cardToCoordinate.get(upper).getVisited().equals(false) &&
                        (placedUnder instanceof ResourceCard || placedUnder instanceof GoldCard)
                        && placedUnder.getSymbol().equals(properColor[1])
                        && cardToCoordinate.get(placedUnder).getVisited().equals(false)
                        && (lower instanceof ResourceCard || lower instanceof GoldCard)
                        && lower.getSymbol().equals(properColor[2])
                        && cardToCoordinate.get(lower).getVisited().equals(false)) {

                    cardToCoordinate.get(upper).setVisited(true);
                    cardToCoordinate.get(placedUnder).setVisited(true);
                    cardToCoordinate.get(lower).setVisited(true);
                    patternsFound++;
                }
            }
        }

        return objCard.getPoints() * patternsFound;
    }
}