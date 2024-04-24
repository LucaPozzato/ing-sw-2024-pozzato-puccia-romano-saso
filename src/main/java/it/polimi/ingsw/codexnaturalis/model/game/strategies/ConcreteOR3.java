package it.polimi.ingsw.codexnaturalis.model.game.strategies;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;

import java.util.Map;

public class ConcreteOR3 implements Strategy{


    @Override
    public int compute(Structure structure) throws IllegalCommandException {
        Map<String, Integer> visibleSymbols = structure.getvisibleSymbols();
        return 2 * (visibleSymbols.get("ANIMAL") / 3);
    }
}
