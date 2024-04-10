package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Printer;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

public class Board {
    private Map<Player, Integer> actualScores;
    private Map<Player, Integer> virtualScores;
    private List<Card> commonObjectives;
    private List<Card> uncoveredCards;
    private List<String> visualBoard;
    int emptyIndex = 0;

    public Board() {
        this.actualScores = new HashMap<>();
        this.virtualScores = new HashMap<>();
        this.commonObjectives = new ArrayList<>();
        this.uncoveredCards = new ArrayList<>();
        this.visualBoard = new ArrayList<>();
    }

    public String getActualScores() {
        String actualScoreString = "";
        for (Map.Entry<Player, Integer> entry : actualScores.entrySet()) {
            actualScoreString += entry.getKey().getNickname() + ": " + entry.getValue() + " | ";
        }
        actualScoreString = actualScoreString.substring(0, actualScoreString.length() - 3);
        return actualScoreString;
    }

    public String getVirtualScores() {
        String virtualScoreString = "";
        for (Map.Entry<Player, Integer> entry : virtualScores.entrySet()) {
            virtualScoreString += entry.getKey().getNickname() + ": " + entry.getValue() + " | ";
        }
        virtualScoreString = virtualScoreString.substring(0, virtualScoreString.length() - 3);
        return virtualScoreString;
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
                this.visualBoard.add(emptyIndex, card.drawDetailedVisual(true));
                emptyIndex++;
                break;
            case 3:
                this.uncoveredCards.add(emptyIndex, card);
                this.visualBoard.add(emptyIndex, card.drawDetailedVisual(true));
                break;
            case 4:
                throw new IllegalCommandException("Cannot add more than 4 cards");
            default:
                break;
        }
    }

    public void removeUncoveredCard(Card card) throws IllegalCommandException {
        if (uncoveredCards.size() < 4)
            throw new IllegalCommandException("Cannot remove more than one card");
        if (!uncoveredCards.contains(card))
            throw new IllegalCommandException("Card is not in hand");

        emptyIndex = uncoveredCards.indexOf(card);
        this.visualBoard.remove(emptyIndex);
        this.uncoveredCards.remove(card);
    }

    public List<String> drawUncoveredCards() throws IllegalCommandException {
        return new Printer().printBoard(visualBoard, uncoveredCards);
    }

    public List<String> drawCommonObjectives() throws IllegalCommandException {
        return new ArrayList<>(List.of(commonObjectives.get(0).drawDetailedVisual(true),
                commonObjectives.get(1).drawDetailedVisual(true)));
    }

    public boolean isLastTurn() {
        return false;
        // definire da dove prendere informazione
    }

}
