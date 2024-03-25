package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.enumerations.*;

public class GoldCard extends Card {
    private String idCard;
    private String symbol;
    private int points;
    private String pointsType;
    private List<String> corners;
    private List<String> requirements;

    public GoldCard(String idCard, String symbol, int points, String pointsType, List<String> corners,
            List<String> requirements) {
        this.idCard = idCard;
        this.symbol = symbol;
        this.points = points;
        this.pointsType = pointsType;
        this.corners = corners;
        this.requirements = requirements;
    }

    public String getIdCard() {
        return idCard;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getPoints() {
        return points;
    }

    public String getPointsType() {
        return pointsType;
    }

    public List<String> getCorners() {
        return corners;
    }

    public List<String> getRequirements() {
        return requirements;
    }

    public void setIdCard(String idCard) {
        this.idCard = idCard;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setPointsType(String pointsType) {
        this.pointsType = pointsType;
    }

    public void setCorners(List<String> corners) {
        this.corners = corners;
    }

    public void setRequirements(List<String> requirements) {
        this.requirements = requirements;
    }

    public void print() {
        System.out.println("id: " + idCard + " symbol: " + symbol + " points: " + points + "pointsType: " + pointsType
                + "corners: " + corners + " requirements" + requirements);
    }
}