package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public class ObjectiveCard extends Card {
    private int points;
    private String shape;
    private String mustHave;
    private Integer divideBy;
    private Integer[] whole3x3;

    // TODO: join the divideBy attribute with the parser class, it's not setted
    // right now

    public ObjectiveCard(String idCard, int points, String shape, String mustHave, Integer divideBy,
            Integer[] whole3x3) {
        super(idCard);
        this.points = points;
        this.shape = shape;
        this.mustHave = mustHave;
        this.divideBy = divideBy;
        this.whole3x3 = whole3x3;
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
    public Integer getDivideBy() throws IllegalCommandException {
        return divideBy;
    }

    @Override
    public Integer[] getWhole3x3() throws IllegalCommandException {
        return whole3x3;
    }

    // TOFIX:
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
        throw new IllegalCommandException();
    }
}
