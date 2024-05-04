package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.io.Serial;
import java.util.List;

public class InitialCard extends Card {
    @Serial
    private static final long serialVersionUID = 156729384019284L;

    private List<String> frontCorners;
    private List<String> frontCenterResources;
    private List<String> backCorners;

    public InitialCard(String idCard, List<String> frontCornerRes, List<String> frontCenterRes,
            List<String> backCornerRes) {
        super(idCard);
        this.frontCorners = frontCornerRes;
        this.frontCenterResources = frontCenterRes;
        this.backCorners = backCornerRes;
    }

    @Override
    public List<String> getFrontCorners() {
        return frontCorners;
    }

    @Override
    public List<String> getBackCorners() {
        return backCorners;
    }

    @Override
    public List<String> getFrontCenterResources() {
        return frontCenterResources;
    }

    public String toString() {
        return "InitialCard{" +
                "idCard='" + idCard + '\'' +
                ", frontCornerRes=" + frontCorners +
                ", frontCenterRes=" + frontCenterResources +
                ", backCornerRes=" + backCorners +
                '}';
    }

    @Override
    public void print() {
        System.out.println("id: " + idCard + "\n\tfrontCornerRes: " + frontCorners + "\n\tfrontCentreRes: "
                + frontCenterResources + "\n\tbackCornerRes: " + backCorners);
    }
}