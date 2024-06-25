package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.io.Serial;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Resource Card in the game, extending the abstract class Card.
 * Contains specific properties and methods related to Resource Cards.
 */
public class ResourceCard extends Card {
    @Serial
    private static final long serialVersionUID = 629038475109384L;

    private String symbol;
    private int points;
    private List<String> frontCorners;
    private List<String> backCorners;

    /**
     * Constructs a Resource Card with the specified properties.
     *
     * @param idCard       The identifier of the card.
     * @param symbol       The symbol associated with the Resource Card.
     * @param points       The points associated with the Resource Card.
     * @param frontCorners The list of front corners associated with the Resource
     *                     Card.
     */
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

    @Override
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

}
