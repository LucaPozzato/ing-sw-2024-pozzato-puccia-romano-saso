package it.polimi.ingsw.codexnaturalis.model.game.strategies;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Triplet;

import java.util.Map;

public class ConcreteOP7 implements Strategy {

    @Override
    public int compute(Structure structure) throws IllegalCommandException {
        Map<Card, Triplet<Integer, Boolean, Boolean>> cardToCoordinate = structure.getCardToCoordinate();
        Card[][] matrix = structure.getCardMatrix();
        int radius = structure.getRadius(structure.getCoordinateToCard());
        int patternsFound = 0;

        for (int i = 40 + radius -1; i >= 40 - radius +2 ; i--) {
            for (int j = 40 - radius; j <= 40 + radius-1; j++) {
                Card upBackrest = matrix[j][i-2];
                Card lowBackrest = matrix[j][i];
                Card seat = matrix[j+1][i+1];
                if(upBackrest.getSymbol().equals("ANIMAL") && !cardToCoordinate.get(upBackrest).getVisited() &&
                        lowBackrest.getSymbol().equals("ANIMAL") && !cardToCoordinate.get(lowBackrest).getVisited() &&
                        seat.getSymbol().equals("SHROOM") && !cardToCoordinate.get(seat).getVisited()){
                    cardToCoordinate.get(upBackrest).setVisited(true);
                    cardToCoordinate.get(lowBackrest).setVisited(true);
                    cardToCoordinate.get(seat).setVisited(true);

                    patternsFound++;
                }
            }
        }
        return 3 * patternsFound;
    }
}
