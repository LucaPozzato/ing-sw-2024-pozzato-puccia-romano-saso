package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

public abstract class Card {
    // TODO: UML change to string -> id is a string
    // UML -> add isIstance
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

    public void print() {
        System.out.println("Card: " + idCard);
    }

    @Override
    public String toString() {
        return "Card: " + idCard;
    }
}
