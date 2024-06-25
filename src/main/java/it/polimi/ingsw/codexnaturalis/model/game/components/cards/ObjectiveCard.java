package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.io.Serial;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

/**
 * Represents an Objective Card in the game, extending the abstract class Card.
 * Contains specific properties and methods related to Objective Cards.
 */
public class ObjectiveCard extends Card {
    @Serial
    private static final long serialVersionUID = 302948176492837L;

    private int points;
    private String shape;
    private String mustHave;
    private Integer divideBy;
    private int[] wholeCells;

    /**
     * Constructs an Objective Card with the specified properties.
     *
     * @param idCard     The identifier of the card.
     * @param points     The points associated with the Objective Card.
     * @param shape      The shape constraint of the Objective Card.
     * @param mustHave   The must-have requirement of the Objective Card.
     * @param divideBy   The division factor associated with the Objective Card.
     * @param wholeCells The array of whole cells associated with the Objective
     *                   Card.
     */
    public ObjectiveCard(String idCard, int points, String shape, String mustHave, Integer divideBy,
            int[] wholeCells) {
        super(idCard);
        this.points = points;
        this.shape = shape;
        this.mustHave = mustHave;
        this.divideBy = divideBy;
        this.wholeCells = wholeCells;
    }

    @Override
    public int getPoints() {
        return points;
    }

    @Override
    public String getShape() {
        return shape;
    }

    @Override
    public String getMustHave() {
        return mustHave;
    }

    @Override
    public Integer getDivideBy_seatColor() throws IllegalCommandException {
        return divideBy;
    }

    @Override
    public int[] getWholeCells() throws IllegalCommandException {
        return wholeCells;
    }

    /**
     * Returns a string representation of the Objective Card.
     *
     * @return A string representation including the identifier, points, shape, and
     *         must-have.
     */
    @Override
    public String toString() {
        return "ObjectiveCard{" +
                "idCard='" + idCard + '\'' +
                ", points=" + points +
                ", shape='" + shape + '\'' +
                ", mustHave='" + mustHave + '\'' +
                '}';
    }

    @Override
    public String getPointsType() throws IllegalCommandException {
        throw new IllegalCommandException("Can't get points type");
    }
}
