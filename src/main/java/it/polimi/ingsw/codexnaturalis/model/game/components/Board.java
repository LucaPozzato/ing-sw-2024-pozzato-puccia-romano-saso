package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class Board {
    private Map<Player, Integer> scores;
    private Map<Player, Integer> totalScores;
    private List<Card> commonObjective;
    private List<Card> uncoveredCards;

    public Board() {
        this.scores = new HashMap<Player, Integer>();
        this.totalScores = new HashMap<Player, Integer>();
        this.uncoveredCards = new ArrayList<Card>();
    }

    public Map<Player, Integer> getScores() {
        return scores;
    }

    public Map<Player, Integer> getTotalScores() {
        return totalScores;
    }

    public List<Card> getCommonObjective() {
        return commonObjective;
    }

    public List<Card> getUncoveredCards() {
        return uncoveredCards;
    }

    public void updateScore(Player player, Integer newPoints) {
        scores.put(player, scores.get(player) + newPoints);
    }

    public void updateTotalScore(Player player, Integer newPoints) {
        totalScores.put(player, totalScores.get(player) + newPoints);
    }

    public void addUncoveredCard(Card card) {
        uncoveredCards.add(card);
    }

    public void removeUncoveredCard(Card card) {
        uncoveredCards.remove(card);
    }

    public boolean isLastTurn() {
        return false;
        // definire da dove prendere informazione
    }

}
