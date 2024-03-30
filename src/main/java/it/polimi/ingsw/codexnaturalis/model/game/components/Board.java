package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class Board {
    private Map<Player, Integer> actualScores;
    private Map<Player, Integer> virtualScores;
    private List<Card> commonObjective;
    private List<Card> uncoveredCards;

    public Board() {
        this.actualScores = new HashMap<>();
        this.virtualScores = new HashMap<>();
        this.commonObjective = new ArrayList<>();
        this.uncoveredCards = new ArrayList<>();
    }

    public Map<Player, Integer> getActualScores() {
        return actualScores;
    }

    public Map<Player, Integer> getVirtualScores() {
        return virtualScores;
    }

    public List<Card> getCommonObjective() {
        return commonObjective;
    }

    public List<Card> getUncoveredCards() {
        return uncoveredCards;
    }

    public void updateActualScore(Player player, Integer newPoints) {
        actualScores.put(player, actualScores.get(player) + newPoints);
    }

    public void updateVirtualScore(Player player, Integer newPoints) {
        virtualScores.put(player, virtualScores.get(player) + newPoints);
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
