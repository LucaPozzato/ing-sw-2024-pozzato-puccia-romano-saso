package it.polimi.ingsw.codexnaturalis.view;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public interface View {
    // minimodel
    public void updateState(String state) throws IllegalCommandException;

    public void updateMyPlayer(Player player) throws IllegalCommandException;

    public void updateCurrentPlayer(Player player) throws IllegalCommandException;

    public void updatePlayers(List<Player> players) throws IllegalCommandException;

    public void updateStructure(List<Structure> structures) throws IllegalCommandException;

    public void updateHand(List<Hand> hands) throws IllegalCommandException;

    public void updateBoard(Board board) throws IllegalCommandException;

    public void updateDeck(Deck deck) throws IllegalCommandException;
}
