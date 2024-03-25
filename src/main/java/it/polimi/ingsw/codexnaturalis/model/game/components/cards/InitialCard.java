package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;

import java.util.List;

public class InitialCard extends Card {
    private String idCard;
    private List<String> frontCornerRes;
    private List<String> frontCentreRes;
    private List<String> backCornerRes;

    public InitialCard(String idCard, List<String> frontCornerRes, List<String> frontCentreRes,
            List<String> backCornerRes) {
        this.idCard = idCard;
        this.frontCornerRes = frontCornerRes;
        this.frontCentreRes = frontCentreRes;
        this.backCornerRes = backCornerRes;
    }

    public String getIdCard() {
        return idCard;
    }

    public List<String> getFrontCornerRes() {
        return frontCornerRes;
    }

    public List<String> getFrontCentreRes() {
        return frontCentreRes;
    }

    public List<String> getBackCornerRes() {
        return backCornerRes;
    }

    public void setFrontCornerRes(List<String> frontCornerRes) {
        this.frontCornerRes = frontCornerRes;
    }

    public void setFrontCentreRes(List<String> frontCentreRes) {
        this.frontCentreRes = frontCentreRes;
    }

    public void setBackCornerRes(List<String> backCornerRes) {
        this.backCornerRes = backCornerRes;
    }

    public String toString() {
        return "InitialCard{" +
                "idCard='" + idCard + '\'' +
                ", frontCornerRes=" + frontCornerRes +
                ", frontCentreRes=" + frontCentreRes +
                ", backCornerRes=" + backCornerRes +
                '}';
    }

    @Override
    public void print() {
        System.out.println("id: " + idCard + "\n\tfrontCornerRes: " + frontCornerRes + "\n\tfrontCentreRes: "
                + frontCentreRes + "\n\tbackCornerRes: " + backCornerRes);
    }
}