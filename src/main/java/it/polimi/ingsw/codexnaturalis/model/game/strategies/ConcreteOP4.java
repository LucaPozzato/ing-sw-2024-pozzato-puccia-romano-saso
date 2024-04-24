package it.polimi.ingsw.codexnaturalis.model.game.strategies;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Triplet;

import java.util.Map;

public class ConcreteOP4 implements Strategy {

    @Override
    public int compute(Structure structure) throws IllegalCommandException {
        Map<Card, Triplet<Integer, Boolean, Boolean>> cardToCoordinate = structure.getCardToCoordinate();
        Card[][] matrix = structure.getCardMatrix();
        int radius = structure.getRadius(structure.getCoordinateToCard());
        int patternsFound = 0;

        for (int i = 40 + radius - 1; i >= 40 - radius + 1; i--) {
            for (int j = 40 - radius + 1 ; j <= 40 + radius -1; j++) {
                Card upDiago = matrix[j+1][i+1];
                Card centre = matrix[j][i];
                Card downDiago = matrix[j-1][i-1];
                if(upDiago.getSymbol().equals("INSECT") && !cardToCoordinate.get(upDiago).getVisited() &&
                        centre.getSymbol().equals("INSECT") && !cardToCoordinate.get(centre).getVisited() &&
                        downDiago.getSymbol().equals("INSECT") && !cardToCoordinate.get(downDiago).getVisited()){
                    cardToCoordinate.get(upDiago).setVisited(true);
                    cardToCoordinate.get(centre).setVisited(true);
                    cardToCoordinate.get(downDiago).setVisited(true);

                    patternsFound++;
                }
            }
        }
        return 2 * patternsFound;
    }
}