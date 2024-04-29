package it.polimi.ingsw.codexnaturalis.model.game.strategies;

import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Triplet;

public class ConcreteOP implements Strategy {
    // Boolean[][] miniMatrix = new Boolean[3][3];

    @Override
    public int compute(Structure structure, Card objcard) throws IllegalCommandException {
        Map<Card, Triplet<Integer, Boolean, Boolean>> cardToCoordinate = structure.getCardToCoordinate();
        Card[][] matrix = structure.getCardMatrix();
        int radius = structure.getRadius(structure.getCoordinateToCard());
        Integer[] whole3x3 = objcard.getWhole3x3();

        int patternsFound = 0;

        for (int i = 40 + radius; i >= 40 - radius - 2; i--) {
            for (int j = 40 - radius; j <= 40 + radius - 2; j++) {
                Card upper = matrix[j + whole3x3[0] % 3][i];
                Card middle = matrix[j + whole3x3[1] % 3][i - 1];
                Card lower = matrix[j + whole3x3[2] % 3][i - 2];

                if (upper != null && upper.getSymbol().equals(objcard.getMustHave())
                        && !cardToCoordinate.get(upper).getVisited() &&
                        middle != null && middle.getSymbol().equals(objcard.getMustHave())
                        && !cardToCoordinate.get(middle).getVisited()
                        && lower != null && lower.getSymbol().equals(objcard.getMustHave())
                        && !cardToCoordinate.get(lower).getVisited()) {
                    cardToCoordinate.get(upper).setVisited(true);
                    cardToCoordinate.get(middle).setVisited(true);
                    cardToCoordinate.get(lower).setVisited(true);

                    patternsFound++;
                }
            }
        }

        return objcard.getPoints() * patternsFound;
    }

    // // for test purpouses
    // // Function that builds the internal minimatrix object
    // private void buildMiniMatrix(Integer[] whole3x3) {
    // int counter = 0;
    // int k = 0;
    // for (int i = 0; i < whole3x3.length; i++) {
    // for (int j = 0; j < whole3x3.length; j++) {
    // if (counter == whole3x3[k]) {
    // miniMatrix[i][j] = true;
    // k++;
    // } else {
    // miniMatrix[i][j] = false;
    // }
    // counter++;
    // }
    // }
    // }

    // // Function that prints the minimatrix
    // private void printMiniMatrix() {
    // for (int i = 0; i < miniMatrix.length; i++) {
    // for (int j = 0; j < miniMatrix.length; j++) {
    // System.out.print("[" + miniMatrix[i][j] + "]");
    // }
    // System.out.print("\n");
    // }
    // }
}