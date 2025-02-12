package it.polimi.ingsw.codexnaturalis.model.game.components.cards;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;

/**
 * Represents an abstract card in the game, providing common methods and
 * properties for all card types.
 * Extends Serializable to support object serialization.
 */
public abstract class Card implements Serializable {
    @Serial
    private static final long serialVersionUID = 817463925078346L;

    protected String idCard;

    /**
     * Constructs a card with a specified identifier.
     *
     * @param idCard The identifier of the card.
     */
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

    public Integer getDivideBy_seatColor() throws IllegalCommandException {
        throw new IllegalCommandException("This card does not have a divideBy");
    }

    public int[] getWholeCells() throws IllegalCommandException {
        throw new IllegalCommandException("This card is not a pattern one");
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

    /**
     * Returns a string representation of the card.
     */
    @Override
    public String toString() {
        return "Card: " + idCard;
    }

}
