package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import it.polimi.ingsw.codexnaturalis.model.enumerations.ObjectiveCardType;

public class ObjectiveCard extends Card {

    private ObjectiveCardType objectiveType;

    public ObjectiveCard(String idCard, String cardFrontImage, String cardBackImage, ObjectiveCardType objectiveType) {
        super(idCard);
        this.objectiveType = objectiveType;
    }

    public ObjectiveCardType getObjectiveType() {
        return objectiveType;
    }

    public void setObjectiveType(ObjectiveCardType objectiveType) {
        this.objectiveType = objectiveType;
    }

}