package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

public class ObjectiveCard extends Card {
    private String idCard;
    private int points;
    private String shape;
    private String mustHave;

    public ObjectiveCard(String idCard, int points, String shape, String mustHave) {
        this.idCard = idCard;
        this.points = points;
        this.shape = shape;
        this.mustHave = mustHave;
    }

    public String getIdCard() {
        return idCard;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getMustHave() {
        return mustHave;
    }

    public void setMustHave(String mustHave) {
        this.mustHave = mustHave;
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
}
