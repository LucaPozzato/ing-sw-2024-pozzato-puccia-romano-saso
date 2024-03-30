package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

import java.util.ArrayList;
import java.util.List;

public class ResourceCard extends Card {
    private String symbol;
    private int points;
    private List<String> frontCorners;
    private List<String> backCorners;
    private boolean visited;

    public ResourceCard(String idCard, String symbol, int points, List<String> frontCorners) {
        super(idCard);
        this.symbol = symbol;
        this.points = points;
        this.frontCorners = frontCorners;
        this.backCorners = new ArrayList<String>();
        for (int i = 0; i < 4; i++) {
            this.backCorners.add("EMPTY");
        }
    }

    @Override
    public String getSymbol() {
        return symbol;
    }

    public int getPoints() {
        return points;
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
    public void print() {
        System.out.println(
                "id: " + idCard + "\n\tsymbol: " + symbol + "\n\tpoints: " + points + "\n\tcorners: " + frontCorners);
    }
    @Override
    public String getPointsType() throws IllegalCommandException {
        throw  new IllegalCommandException();
    }
}
