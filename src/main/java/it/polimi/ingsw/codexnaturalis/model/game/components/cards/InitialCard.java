package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;

public class InitialCard extends Card {

    // modifiche da rivedere/inserire su uml
    private Resource[] frontCardCorners;
    private Resource[] backCardResources;
    private boolean[] backCardCorners;

    public InitialCard(String idCard, String cardFrontImage, String cardBackImage, Resource[] frontCardCorners,
            Resource[] backCardResources, boolean[] backCardCorners) {
        super(idCard);
        this.frontCardCorners = frontCardCorners;
        this.backCardResources = backCardResources;
        this.backCardCorners = backCardCorners;
    }

    public Resource[] getFrontCardCorners() {
        return frontCardCorners;
    }

    public Resource[] getBackCardResources() {
        return backCardResources;
    }

    public boolean[] getBackCardCorners() {
        return backCardCorners;
    }

    public void setFrontCardCorners(Resource[] frontCardCorners) {
        this.frontCardCorners = frontCardCorners;
    }

    public void setBackCardResources(Resource[] backCardResources) {
        this.backCardResources = backCardResources;
    }

    public void setBackCardCorners(boolean[] backCardCorners) {
        this.backCardCorners = backCardCorners;
    }

}