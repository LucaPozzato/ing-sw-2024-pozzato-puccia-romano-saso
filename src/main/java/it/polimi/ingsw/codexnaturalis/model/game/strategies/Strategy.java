package it.polimi.ingsw.codexnaturalis.model.game.strategies;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;

public interface Strategy {
    /**
     * Offers the signature of the method each concrete strategy which implements Strategy has to override.
     * The goal is to pass to the entity delegated to compute the amount of points made by each player a structure containing only Strategy in order to make it agnostic regarding the type of concrete strategies it's dealing with
     * @param structure the player structure
     * @param objCard the objective we want to look for on the player structure
     * @return the number of points made thanks to objective satisfaction
     * @throws IllegalCommandException
     */
    int compute(Structure structure, Card objCard) throws IllegalCommandException;
}
