package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class Board {
    private Map<Player, Integer> actualScores;
    private Map<Player, Integer> virtualScores;
    private List<Card> commonObjectives;
    private List<Card> uncoveredCards;
    private int emptyIndex = 0;

    public Board() {
        this.actualScores = new HashMap<>();
        this.virtualScores = new HashMap<>();
        this.commonObjectives = new ArrayList<>();
        this.uncoveredCards = new ArrayList<>();
    }

    public Integer getActualPoints(Player player) {
        return actualScores.get(player);
    }

    public Integer getVirtualPoints(Player player) {
        return virtualScores.get(player);
    }

    public Map<Player, Integer> getActualScores() {
        return actualScores;
    }

    public Map<Player, Integer> getVirtualScores() {
        return virtualScores;
    }

    public List<Card> getCommonObjectives() {
        return commonObjectives;
    }

    public List<Card> getUncoveredCards() {
        return uncoveredCards;
    }

    public void setCommonObjectives(List<Card> commonObjectives) {
        this.commonObjectives = commonObjectives;
    }

    public void updateActualScore(Player player, Integer newPoints) throws IllegalCommandException {
        if (!actualScores.containsKey(player)) {
            if (actualScores.size() < 4)
                actualScores.put(player, 0);
            else
                throw new IllegalCommandException("Cannot have more than 4 players");
        } else
            actualScores.put(player, actualScores.get(player) + newPoints);
    }

    public void updateVirtualScore(Player player, Integer newPoints) throws IllegalCommandException {
        if (!virtualScores.containsKey(player)) {
            if (virtualScores.size() < 4)
                virtualScores.put(player, 0);
            else
                throw new IllegalCommandException("Cannot have more than 4 players");
        } else
            virtualScores.put(player, virtualScores.get(player) + newPoints);
    }

    public void addUncoveredCard(Card card) throws IllegalCommandException {
        if (card instanceof InitialCard || card instanceof ObjectiveCard)
            throw new IllegalCommandException("Cannot add card, wrong card type");

        switch (this.uncoveredCards.size()) {
            case 0, 1, 2:
                this.uncoveredCards.add(emptyIndex, card);
                emptyIndex++;
                break;
            case 3:
                this.uncoveredCards.add(emptyIndex, card);
                break;
            case 4:
                throw new IllegalCommandException("Board is full");
            default:
                break;
        }
    }

    public void removeUncoveredCard(Card card) throws IllegalCommandException {
        if (uncoveredCards.size() < 4)
            throw new IllegalCommandException("Cannot remove more than one card");
        if (!uncoveredCards.contains(card))
            throw new IllegalCommandException("Card is not in board");

        emptyIndex = uncoveredCards.indexOf(card);
        this.uncoveredCards.remove(card);
    }

    public boolean isLastTurn() {
        return false;
        // definire da dove prendere informazione
    }

}
