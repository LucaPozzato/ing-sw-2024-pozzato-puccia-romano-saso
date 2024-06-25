package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.io.Serial;
import java.util.List;

/**
 * Represents an Initial Card in the game, extending the abstract class Card.
 * Contains specific properties and methods related to Initial Cards.
 */
public class InitialCard extends Card {
    @Serial
    private static final long serialVersionUID = 156729384019284L;

    private List<String> frontCorners;
    private List<String> frontCenterResources;
    private List<String> backCorners;

    /**
     * Constructs an Initial Card with the specified properties.
     *
     * @param idCard         The identifier of the card.
     * @param frontCornerRes The list of front corners associated with the card.
     * @param frontCenterRes The list of front center resources associated with the
     *                       card.
     * @param backCornerRes  The list of back corners associated with the card.
     */
    public InitialCard(String idCard, List<String> frontCornerRes, List<String> frontCenterRes,
                       List<String> backCornerRes) {
        super(idCard);
        this.frontCorners = frontCornerRes;
        this.frontCenterResources = frontCenterRes;
        this.backCorners = backCornerRes;
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
    public List<String> getFrontCenterResources() {
        return frontCenterResources;
    }

    /**
     * Returns a string representation of the Initial Card.
     *
     * @return A string representation including the identifier and lists of corners
     * and resources.
     */
    public String toString() {
        return "InitialCard{" +
                "idCard='" + idCard + '\'' +
                ", frontCornerRes=" + frontCorners +
                ", frontCenterRes=" + frontCenterResources +
                ", backCornerRes=" + backCorners +
                '}';
    }
}