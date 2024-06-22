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
     * The method uses the cardMatrix and the wholeCells card attribute in order to
     * identify the 3 cards which compose the stair pattern in a 3x3 matrix.
     * Then the reduced matrix computed using radius is visited by letting scroll
     * the 3x3 matrix with the highlighted cells on it and counting the matches in
     * patternFound.
     * 
     * @param structure the player structure to get the cardmatrix and the
     *                  cardToCoordinates
     * @param objCard   the stair objective we want to look for on the player
     *                  structure
     * @return the number of points made due to objective satisfaction
     * @throws IllegalCommandException
     */
    @Override
    public int compute(Structure structure, Card objCard) throws IllegalCommandException {

        // System.out.println("SIAMO ENTRATI IN COMPUTE!!!!!!!");
        // System.out.println("La carta che cerchiamo è: " + objCard.getIdCard());
        // System.out.println("La lista di carte piazzate sulla struttura è: ");

        // for (Pair<Card, Boolean> pairo : structure.getPlacedCards()) {
        // System.out.print(pairo.getKey().getIdCard());
        // }

        Map<Card, Triplet<Integer, Boolean, Boolean>> cardToCoordinate = structure.getCardToCoordinate();
        // System.out.println("cardToCoordinate: ");
        // for (Card key : cardToCoordinate.keySet()) {
        // System.out.print("Key: " + key.getIdCard() + "coordinate" +
        // cardToCoordinate.get(key).getFirst());
        // }

        Card[][] matrix = structure.getCardMatrix();
        // System.out.println("Matrix: ");
        // for (int i = 0; i < matrix.length; i++) {
        // for (int j = 0; j < matrix[0].length; j++) {
        // System.out.print("[ " + matrix[i][j] + "]");
        // }
        // System.out.print("\n");
        // }

        int radius = structure.getRadius(structure.getCoordinateToCard());
        // System.out.println("Radius: " + radius);

        int[] wholeCells = objCard.getWholeCells();
        // System.out.println("WholeCells: ");
        // for (int intero : wholeCells) {
        // System.out.print("[" + intero + "], ");
        // }

        int patternsFound = 0;
        for (int i = 40 + radius; i >= 40 - radius + 2; i--) {
            for (int j = 40 - radius; j <= 40 + radius - 2; j++) {

                Card upper = matrix[j + wholeCells[0] % 3][i];
                // System.out.print("[Upper: " + upper.getIdCard() + ", Coordinata: " +
                // cardToCoordinate.get(upper));
                Card middle = matrix[j + wholeCells[1] % 3][i - 1];
                // System.out.print(", middle: " + middle.getIdCard()+ ", Coordinata: " +
                // cardToCoordinate.get(middle));
                Card lower = matrix[j + wholeCells[2] % 3][i - 2];
                // System.out.print(", lower: " + lower.getIdCard()+ ", Coordinata: " +
                // cardToCoordinate.get(lower));
                // System.out.println("]\n");

                if ((upper instanceof ResourceCard || upper instanceof GoldCard)
                        && upper.getSymbol().equals(objCard.getMustHave())
                        && !cardToCoordinate.get(upper).getVisited() &&
                        (middle instanceof ResourceCard || middle instanceof GoldCard)
                        && middle.getSymbol().equals(objCard.getMustHave())
                        && !cardToCoordinate.get(middle).getVisited()
                        && (lower instanceof ResourceCard || lower instanceof GoldCard)
                        && lower.getSymbol().equals(objCard.getMustHave())
                        && !cardToCoordinate.get(lower).getVisited()) {

                    // System.out.println("Pattern Trovato");
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