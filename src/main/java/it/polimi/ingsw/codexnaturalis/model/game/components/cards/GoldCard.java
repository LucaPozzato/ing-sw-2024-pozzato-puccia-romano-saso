package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GoldCard extends Card {
    private String symbol;
    private int points;
    private String pointsType;
    private List<String> frontCorners;
    private List<String> backCorners;
    private Map<String, Integer> requirements;

    public GoldCard(String idCard, String symbol, int points, String pointsType, List<String> frontCorners,
            Map<String, Integer> requirements) {
        super(idCard);
        this.symbol = symbol;
        this.points = points;
        this.pointsType = pointsType;
        this.frontCorners = frontCorners;
        this.backCorners = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            this.backCorners.add("EMPTY");
        }
        this.requirements = requirements;
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    public int getPoints() {
        return points;
    }

    public String getPointsType() {
        return pointsType;
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
    public Map<String, Integer> getRequirements() {
        return requirements;
    }

    @Override
    public void print() {
        System.out.println(
                "id: " + idCard + "\n\tsymbol: " + symbol + "\n\tpoints: " + points + "\n\tpointsType: " + pointsType
                        + "\n\tcorners: " + frontCorners + "\n\trequirements: ");
        for (Map.Entry<String, Integer> entry : requirements.entrySet()) {
            System.out.println("\t\t" + entry.getKey() + ": " + entry.getValue());
        }
    }
}