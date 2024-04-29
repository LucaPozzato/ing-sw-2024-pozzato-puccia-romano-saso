package it.polimi.ingsw.codexnaturalis.model.game.strategies;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;

public interface Strategy {
    int compute(Structure structure, Card objcard) throws IllegalCommandException;
}
