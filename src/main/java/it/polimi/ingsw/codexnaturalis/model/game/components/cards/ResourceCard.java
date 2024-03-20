package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.util.List;

public class ResourceCard extends Card {

    private String cardType;
    private List<String> cardCorners;
    private int cardPoints;

    public ResourceCard(String idCard, String cardType,
            List<String> cardCorners, int cardPoints) {
        super(idCard); // check super
        this.cardType = cardType;
        this.cardCorners = cardCorners;
        this.cardPoints = cardPoints;
    }

    public String getCardType() {
        return cardType;
    }

    public List<String> getCardCorners() {
        return cardCorners;
    }

    public int getCardPoints() {
        return cardPoints;
    }

}
