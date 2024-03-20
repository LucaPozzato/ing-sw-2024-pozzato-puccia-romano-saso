package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.util.List;

import it.polimi.ingsw.codexnaturalis.model.enumerations.*;

public class GoldCard extends Card {

    private String cardType;
    private List<String> cardCorners;
    private List<String> resourceRequired; // uml segna 4
    private int cardPoints;
    private String pointType;

    public GoldCard(String idCard, String cardType,
            String pointType, List<String> cardCorners, List<String> resourceRequired, int cardPoints) {
        super(idCard);
        this.cardType = cardType;
        this.cardCorners = cardCorners;
        this.resourceRequired = resourceRequired;
        this.cardPoints = cardPoints;
        this.pointType = pointType;
    }

    public String getCardType() {
        return cardType;
    }

    public List<String> getCardCorners() {
        return cardCorners;
    }

    public List<String> getResourceRequired() {
        return resourceRequired;
    }

    public int getCardPoints() {
        return cardPoints;
    }

    public String getPointType() {
        return pointType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public void setCardCorners(List<String> cardCorners) {
        this.cardCorners = cardCorners;
    }

    public void setResourceRequired(List<String> resourceRequired) {
        this.resourceRequired = resourceRequired;
    }

    public void setCardPoints(int cardPoints) {
        this.cardPoints = cardPoints;
    }

    public void setPointType(String pointType) {
        this.pointType = pointType;
    }
}