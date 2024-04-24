package it.polimi.ingsw.codexnaturalis.model.game.strategies;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;

import java.util.Map;

public class ConcreteOR5 implements Strategy{


    @Override
    public int compute(Structure structure) throws IllegalCommandException {
        Map<String, Integer> visibleSymbols = structure.getvisibleSymbols();
        return 3 * (min(visibleSymbols.get("SCROLL"), visibleSymbols.get("INK"),
                visibleSymbols.get("FEATHER")));
    }

    private int min(int x, int y, int z) throws IllegalCommandException {
        if (x <= y && x <= z)
            return x;
        else if (y <= x && y <= z)
            return y;
        else if (z <= x && z <= y)
            return z;
        else
            throw new IllegalCommandException("Uncomparable numbers");
    }

}

