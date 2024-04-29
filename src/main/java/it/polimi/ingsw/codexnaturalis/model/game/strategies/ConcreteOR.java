package it.polimi.ingsw.codexnaturalis.model.game.strategies;

import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;

public class ConcreteOR implements Strategy {

    @Override
    public int compute(Structure structure, Card objcard) throws IllegalCommandException {

        Map<String, Integer> visibleSymbols = structure.getvisibleSymbols();

        if (objcard.getMustHave().equals("FOLDEDHANDS")) {
            return 3 * (min(visibleSymbols.get("SCROLL"), visibleSymbols.get("INK"),
                    visibleSymbols.get("FEATHER")));
        } else {
            return 2 * (visibleSymbols.get(objcard.getMustHave()) / objcard.getDivideBy());
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