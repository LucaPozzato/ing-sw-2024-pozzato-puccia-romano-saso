package it.polimi.ingsw.codexnaturalis.view;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.chat.Chat;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public interface View {
    public void run();

    public void updateChat(Chat chat);

    public void updateState(String state);

    public void updateMyPlayer(Player player);

    public void updateWinners(List<Player> winners);

    public void updateCurrentPlayer(Player player);

    public void updatePlayers(List<Player> players);

    public void updateStructures(List<Structure> structures);

    public void updateHand(List<Hand> hands);

    public void updateBoard(Board board);

    public void updateDeck(Deck deck);

    public void updateError(String error);
}
