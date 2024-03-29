package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.*;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class Board {

    private HashMap<Player, Integer> scores;
    private List<Card> commonObjective;
    private List<Card> uncoveredCards;

    public Board() {
        this.scores = new HashMap<Player, Integer>();
        this.uncoveredCards = new ArrayList<Card>();
    }

    public HashMap<Player, Integer> getScores() {
        return scores;
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
