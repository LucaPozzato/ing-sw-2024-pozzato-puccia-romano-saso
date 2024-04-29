package it.polimi.ingsw.codexnaturalis.view;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;

public interface View {
    // minimodel

    public void updateStructure(Structure structure) throws IllegalCommandException;

    public void updateHand(Hand hand) throws IllegalCommandException;

    public void updateBoard(Board board) throws IllegalCommandException;

    public void updateDeck(Deck deck) throws IllegalCommandException;
}
