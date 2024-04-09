package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public abstract class Card {
    protected String idCard;

    public Card(String idCard) {
        this.idCard = idCard;
    };

    public String getIdCard() {
        return idCard;
    }

    public String getSymbol() throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have a symbol");
    }

    public String getMustHave() throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have a musthave");
    }

    public String getShape() throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have a shape");
    }

    public List<String> getFrontCorners() throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have front corners");
    }

    public List<String> getBackCorners() throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have bottom corners");
    }

    public List<String> getFrontCenterResources() throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have center resources");
    }

    public Map<String, Integer> getRequirements() throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have requirements");
    }

    public int getPoints() throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have points");
    }

    public String getPointsType() throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have points type");
    }

    public void print() {
        System.out.println("Card: " + idCard);
    }

    public char[][] drawVisual(char[][] drawingBoard, int x, int y, Boolean side) throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have a visual representation");
    }

    public String drawDetailedVisual(Boolean side)
            throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have a detailed visual representation");
    }

    @Override
    public String toString() {
        return "Card: " + idCard;
    }

}
