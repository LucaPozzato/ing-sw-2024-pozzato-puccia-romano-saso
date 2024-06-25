package it.polimi.ingsw.codexnaturalis.model.game.strategies;

import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;

public class ConcreteOR implements Strategy {

    /**
     * The method gets an objective card and, by looking at the visible resources related to the specific player structure, it computes the points made satisfying that objective. <br>
     * Note that visibleSymbol is computed every placedCard, so it has the updated resource and symbol count each moment of the match
     *
     * @param structure the player structure used to get the visibleSymbols structure's attribute
     * @param objCard   the objective we want to look for on the player structure
     * @return the number of points made thanks to objective satisfaction
     * @throws IllegalCommandException
     */
    @Override
    public int compute(Structure structure, Card objCard) throws IllegalCommandException {

        Map<String, Integer> visibleSymbols = structure.getvisibleSymbols();

        if (objCard.getMustHave().equals("FOLDEDHANDS")) {
            return 3 * (min(visibleSymbols.get("SCROLL"), visibleSymbols.get("INK"),
                    visibleSymbols.get("FEATHER")));
        } else {
            return 2 * (visibleSymbols.get(objCard.getMustHave()) / objCard.getDivideBy_seatColor());
        }
    }

    private int min(int x, int y, int z) throws IllegalCommandException {
        if (x <= y && x <= z)
            return x;
        else if (y <= x && y <= z)
            return y;
        else
            return z;
    }
}