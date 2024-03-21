package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

public abstract class Card {
    // TODO: UML change to string -> id is a string
    // UML -> add isIstance
    protected String idCard;

    public String getIdCard() {
        return idCard;
    }

    public boolean isInstance(Class<? extends Card> card) {
        return card.isInstance(this);
    }

    public void print() {
        System.out.println("Card: " + idCard);
    }
}
