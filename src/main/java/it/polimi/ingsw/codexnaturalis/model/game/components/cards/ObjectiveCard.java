package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

import java.io.Serial;

public class ObjectiveCard extends Card {
    @Serial
    private static final long serialVersionUID = 302948176492837L;

    private int points;
    private String shape;
    private String mustHave;
    private Integer divideBy;
    private int[] wholeCells;

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

    // TOFIX: add new attributes
    @Override
    public String toString() {
        return "ObjectiveCard{" +
                "idCard='" + idCard + '\'' +
                ", points=" + points +
                ", shape='" + shape + '\'' +
                ", mustHave='" + mustHave + '\'' +
                '}';
    }

    // TOFIX:
    @Override
    public void print() {
        System.out.println(
                "id: " + idCard + "\n\tpoints: " + points + "\n\tshape: " + shape + "\n\tmustHave: " + mustHave);
    }

    @Override
    public String getPointsType() throws IllegalCommandException {
        throw new IllegalCommandException("Can't get points type");
    }
}
