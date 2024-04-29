package it.polimi.ingsw.codexnaturalis.network.events;

import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.structure.Structure;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;
import it.polimi.ingsw.codexnaturalis.network.MiniModel;

public class StartGameEvent extends Event {
    private List<Player> players;
    private List<Structure> playerStructure;
    private List<Card> uncoveredCards;
    private List<Card> commonObjectives;
    private Map<Player, Integer> actualScores;
    private Player currentPlayer;
    private Player nextPlayer;

    public StartGameEvent(List<Player> players, List<Structure> playerStructure, List<Card> uncoveredCards,
            List<Card> commonObjectives, Map<Player, Integer> actualScores, Player currentPlayer, Player nextPlayer) {
        this.players = players;
        this.playerStructure = playerStructure;
        this.uncoveredCards = uncoveredCards;
        this.commonObjectives = commonObjectives;
        this.actualScores = actualScores;
        this.currentPlayer = currentPlayer;
        this.nextPlayer = nextPlayer;
    }

    public void doJob(MiniModel miniModel) throws IllegalCommandException {
        miniModel.setPlayers(players);
        miniModel.setPlayerStructure(playerStructure);
        miniModel.setUncoveredCards(uncoveredCards);
        miniModel.setActualScore(actualScores);
        miniModel.setCommonObjectives(commonObjectives);
        miniModel.setCurrentPlayer(currentPlayer);
        miniModel.setNextPlayer(nextPlayer);
    }
}