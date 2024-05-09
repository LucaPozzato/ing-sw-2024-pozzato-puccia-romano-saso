package it.polimi.ingsw.codexnaturalis.network.events;

import java.io.Serial;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.Board;
import it.polimi.ingsw.codexnaturalis.model.game.components.Deck;
import it.polimi.ingsw.codexnaturalis.model.game.components.Hand;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.client.MiniModel;

public class JoinGameEvent extends Event {
    @Serial
    private static final long serialVersionUID = 333303916472093L;

    private String clientId;
    private String state;
    private List<Player> players;
    private List<Structure> playerStructure;
    private List<Hand> hands;
    private Board board;
    private Deck deck;
    private Player currentPlayer;
    private Player nextPlayer;

    public JoinGameEvent(String clientId, String state, List<Player> players, List<Structure> playerStructure,
            List<Hand> hands,
            Board board, Deck deck, Player currentPlayer, Player nextPlayer) {
        this.clientId = clientId;
        this.state = state;
        this.players = players;
        this.playerStructure = playerStructure;
        this.hands = hands;
        this.board = board;
        this.deck = deck;
        this.currentPlayer = currentPlayer;
        this.nextPlayer = nextPlayer;
    }

    @Override
    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setPlayers(players);
        miniModel.setPlayerStructure(playerStructure);
        miniModel.setHands(hands);
        miniModel.setBoard(board);
        miniModel.setDeck(deck);
        miniModel.setCurrentPlayer(currentPlayer);
        miniModel.setNextPlayer(nextPlayer);
        miniModel.setState(state);
    }

    @Override
    public String getClientId() {
        return this.clientId;
    }
}