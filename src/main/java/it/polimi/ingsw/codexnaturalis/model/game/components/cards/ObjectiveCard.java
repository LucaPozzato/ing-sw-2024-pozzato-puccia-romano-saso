package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public class ObjectiveCard extends Card {
    private int points;
    private String shape;
    private String mustHave;

    public ObjectiveCard(String idCard, int points, String shape, String mustHave) {
        super(idCard);
        this.points = points;
        this.shape = shape;
        this.mustHave = mustHave;
    }

    public int getPoints() {
        return points;
    }

    public String getShape() {
        return shape;
    }

    public String getMustHave() {
        return mustHave;
    }

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
    public void print() {
        System.out.println(
                "id: " + idCard + "\n\tpoints: " + points + "\n\tshape: " + shape + "\n\tmustHave: " + mustHave);
    }

    @Override
    public boolean isVisited() throws IllegalCommandException {
        throw new IllegalCommandException();
    }
    @Override
    public void visit() throws IllegalCommandException {
        throw new IllegalCommandException();
    }
}
