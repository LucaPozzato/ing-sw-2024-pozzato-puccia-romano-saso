package it.polimi.ingsw.codexnaturalis.model.game.components;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.player.Player;

/**
 * Represents the board of the game, including scores, objectives, and uncovered
 * cards.
 * Implements Serializable to support object serialization.
 */
public class Board implements Serializable {
    @Serial
    private static final long serialVersionUID = 709863251847392L;
    private Map<Player, Integer> actualScores;
    private Map<Player, Integer> virtualScores;
    private List<Card> commonObjectives;
    private List<Card> uncoveredCards;
    private int emptyIndex = 0;

    /**
     * Constructs a new Board with initial values.
     */
    public Board() {
        this.actualScores = new HashMap<>();
        this.virtualScores = new HashMap<>();
        this.commonObjectives = new ArrayList<>();
        this.uncoveredCards = new ArrayList<>();
    }

    public Integer getActualPoints(Player player) {
        return actualScores.get(player);
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

    /**
     * Updates the actual score of a player.
     *
     * @param player    The player whose score is to be updated.
     * @param newPoints The points to add to the player's actual score.
     * @throws IllegalCommandException If an attempt is made to exceed the maximum
     *                                 number of players.
     */
    public void updateActualScore(Player player, Integer newPoints) throws IllegalCommandException {
        if (!actualScores.containsKey(player)) {
            if (actualScores.size() < 4)
                actualScores.put(player, 0);
            else
                throw new IllegalCommandException("Cannot have more than 4 players");
        } else
            actualScores.put(player, actualScores.get(player) + newPoints);
    }

    /**
     * Updates the virtual score of a player.
     *
     * @param player    The player whose score is to be updated.
     * @param newPoints The points to add to the player's virtual score.
     * @throws IllegalCommandException If an attempt is made to exceed the maximum
     *                                 number of players.
     */
    public void updateVirtualScore(Player player, Integer newPoints) throws IllegalCommandException {
        if (!virtualScores.containsKey(player)) {
            if (virtualScores.size() < 4)
                virtualScores.put(player, 0);
            else
                throw new IllegalCommandException("Cannot have more than 4 players");
        } else
            virtualScores.put(player, virtualScores.get(player) + newPoints);
    }

    /**
     * Adds an uncovered card to the board.
     *
     * @param card The card to be added to the uncovered cards list.
     * @throws IllegalCommandException If an attempt is made to add an invalid card
     *                                 type or if the board is full.
     */

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

    /**
     * Removes an uncovered card from the board.
     *
     * @param card The card to be removed from the uncovered cards list.
     * @throws IllegalCommandException If an attempt is made to remove more than one
     *                                 card or if the card is not on the board.
     */
    public void removeUncoveredCard(Card card) throws IllegalCommandException {
        if (uncoveredCards.size() < 4)
            throw new IllegalCommandException("Cannot remove more than one card");
        if (!uncoveredCards.contains(card))
            throw new IllegalCommandException("Card is not in board");

        emptyIndex = uncoveredCards.indexOf(card);
        this.uncoveredCards.remove(card);
    }
}
