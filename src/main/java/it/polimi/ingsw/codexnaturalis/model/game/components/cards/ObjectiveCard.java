package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

public class ObjectiveCard extends Card{
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

    @Override
    public String toString() {
        return "ObjectiveCard{" +
                "idCard='" + idCard + '\'' +
                ", points=" + points +
                ", shape='" + shape + '\'' +
                ", mustHave='" + mustHave + '\'' +
                '}';
    }

}
