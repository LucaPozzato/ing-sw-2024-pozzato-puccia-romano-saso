package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Represents a Gold Card in the game, extending the abstract class Card.
 * Contains specific properties and methods related to Gold Cards.
 */
public class GoldCard extends Card {
    @Serial
    private static final long serialVersionUID = 284719356018473L;
    private String symbol;
    private int points;
    private String pointsType;
    private List<String> frontCorners;
    private List<String> backCorners;
    private Map<String, Integer> requirements;

    /**
     * Constructs a Gold Card with the specified properties.
     *
     * @param idCard       The identifier of the card.
     * @param symbol       The symbol associated with the card.
     * @param points       The points value of the card.
     * @param pointsType   The type of points.
     * @param frontCorners The list of front corners associated with the card.
     * @param requirements The requirements associated with the card.
     */
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

    @Override
    public int getPoints() {
        return points;
    }

    @Override
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

}