package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Objects;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import javafx.util.Pair;

public class Structure {
    private List<Card> timeStamp;
    private Map<Card, Pair<Integer, Boolean>> cardToCoordinate;
    private Map<Integer, Pair<Card, Boolean>> coordinateToCard;
    private Integer destinationCoord;
    private Map<String, Integer> visibleSymbols;
    private char[][] structure = new char[170][498];

    public Structure() {
        this.timeStamp = new ArrayList<>();
        this.cardToCoordinate = new HashMap<>();
        this.coordinateToCard = new HashMap<>();
        this.visibleSymbols = new HashMap<>(
                Map.of("VEGETABLE", 0, "ANIMAL", 0, "INSECT", 0, "SHROOM", 0, "INK", 0, "SCROLL", 0,
                        "FEATHER", 0, "EMPTY", 0, "NULL", 0));
    }

    // frontUp = true -> front of the card is visible
    // frontUp = false -> back of the card is visible
    public void placeCard(Card father, Card card, String position, Boolean frontUp) throws IllegalCommandException {
        // Calculates coordinates of new card
        destinationCoord = calcCoordinate(father, position);
        // Checks if the card can be placed and places it
        if (isPlaceable(father, card, destinationCoord, position)) {
            if (card instanceof InitialCard) {
                cardToCoordinate.put(card, new Pair<Integer, Boolean>(4040, frontUp));
                coordinateToCard.put(4040, new Pair<Card, Boolean>(card, frontUp));
                timeStamp.add(card);
            } else {
                cardToCoordinate.put(card, new Pair<Integer, Boolean>(destinationCoord, frontUp));
                coordinateToCard.put(destinationCoord, new Pair<Card, Boolean>(card, frontUp));
                timeStamp.add(card);
            }
            calcVisibleSymbols(father, card, position, frontUp);
        } else {
            throw new IllegalCommandException("Card cannot be placed");
        }
    }

    public void calcStairPattern() {

    }

    public void calcChairPattern() {

    }

    private void calcVisibleSymbols(Card father, Card card, String position, Boolean frontUp)
            throws IllegalCommandException {
        if (frontUp) {
            for (String symbol : card.getFrontCorners()) {
                visibleSymbols.put(symbol, visibleSymbols.get(symbol) + 1);
            }
            if (card instanceof InitialCard) {
                for (String symbol : card.getFrontCenterResources()) {
                    visibleSymbols.put(symbol, visibleSymbols.get(symbol) + 1);
                }
            }
        } else {
            if (card instanceof InitialCard) {
                for (String symbol : card.getBackCorners()) {
                    visibleSymbols.put(symbol, visibleSymbols.get(symbol) + 1);
                }
            } else {
                visibleSymbols.put(card.getSymbol(), visibleSymbols.get(card.getSymbol()) + 1);
            }
        }

        if (father == null)
            return;

        List<String> fFrontCorners = father.getFrontCorners();
        List<String> fBackCorners = father.getBackCorners();

        switch (position) {
            case "TL":
                if (cardToCoordinate.get(father).getValue())
                    visibleSymbols.put(fFrontCorners.get(0), visibleSymbols.get(fFrontCorners.get(0)) - 1);
                else
                    visibleSymbols.put(fBackCorners.get(0), visibleSymbols.get(fBackCorners.get(0)) - 1);
                break;
            case "TR":
                if (cardToCoordinate.get(father).getValue())
                    visibleSymbols.put(fFrontCorners.get(1), visibleSymbols.get(fFrontCorners.get(1)) - 1);
                else
                    visibleSymbols.put(fBackCorners.get(1), visibleSymbols.get(fBackCorners.get(1)) - 1);
                break;
            case "BL":
                if (cardToCoordinate.get(father).getValue())
                    visibleSymbols.put(fFrontCorners.get(2), visibleSymbols.get(fFrontCorners.get(2)) - 1);
                else
                    visibleSymbols.put(fBackCorners.get(2), visibleSymbols.get(fBackCorners.get(2)) - 1);
                break;
            case "BR":
                if (cardToCoordinate.get(father).getValue())
                    visibleSymbols.put(fFrontCorners.get(3), visibleSymbols.get(fFrontCorners.get(3)) - 1);
                else
                    visibleSymbols.put(fBackCorners.get(3), visibleSymbols.get(fBackCorners.get(3)) - 1);
                break;
            default:
                break;
        }
    }

    public Map<String, Integer> getVisibleResources() {
        Map<String, Integer> visibleResources = new HashMap<>();
        for (Resource resource : Resource.values()) {
            visibleResources.put(resource.name(), visibleSymbols.get(resource.name()));
        }
        return visibleResources;
    }

    public Map<String, Integer> getVisibleObjects() {
        Map<String, Integer> visibleObjects = new HashMap<>();
        for (Objects resource : Objects.values()) {
            visibleObjects.put(resource.name(), visibleSymbols.get(resource.name()));
        }
        return visibleObjects;
    }

    /*
     * Verifies if the card can be placed in the structure
     * 
     * @param father the father card
     * 
     * @param coordinate the coordinate of the new card
     * 
     * @param position the position of the new card
     * 
     * @return true if the card can be placed, false otherwise
     */
    private Boolean isPlaceable(Card father, Card card, Integer coordinate, String position)
            throws IllegalCommandException {
        // checks if initial card is placeable
        if (card instanceof InitialCard) {
            if (coordinateToCard.containsKey(4040))
                return false;
            else
                return true;
        }

        // Checks if the father card is present
        if (father == null)
            return false;

        // Checks if another card is already placed in that coordinate
        if (coordinateToCard.containsKey(coordinate))
            return false;

        // Checks if the gold card has the requirements to be placed
        if (card instanceof GoldCard) {
            for (Map.Entry<String, Integer> entry : card.getRequirements().entrySet()) {
                if (visibleSymbols.get(entry.getKey()) < entry.getValue())
                    return false;
            }
        }

        // Gets correct corners of correct placedFront of the father card
        List<String> corners;

        if (cardToCoordinate.get(father).getValue())
            corners = father.getFrontCorners();
        else
            corners = father.getBackCorners();

        // Checks if the card can be placed in the coordinates if the bottom card's
        // angle is visible
        switch (position) {
            case "TL":
                if (corners.get(0).equals("NULL"))
                    return false;
                break;
            case "TR":
                if (corners.get(1).equals("NULL"))
                    return false;
                break;
            case "BL":
                if (corners.get(2).equals("NULL"))
                    return false;
                break;
            case "BR":
                if (corners.get(3).equals("NULL"))
                    return false;
                break;
            default:
                break;
        }

        return true;
    }

    /*
     * Calculates the coordinate of the new card
     * 
     * @param father the father card
     * 
     * @param position the position of the new card
     * 
     * @return the coordinate of the new card
     */
    private Integer calcCoordinate(Card father, String position) {
        if (father == null)
            return 0;

        Integer fatherCoordinate = cardToCoordinate.get(father).getKey();
        switch (position) {
            case "TL":
                return fatherCoordinate - 99;
            case "TR":
                return fatherCoordinate + 101;
            case "BL":
                return fatherCoordinate - 101;
            case "BR":
                return fatherCoordinate + 99;
            default:
                return 0;
        }
    }

    public void printSkeleton() {
    }

    // structure is a matrix of 249x85
    public void print() throws IllegalCommandException {
    }

    private void addCardToVisual(Card card, int x, int y, Boolean frontUp) throws IllegalCommandException {
        List<String> corners;

        if (frontUp) {
            corners = card.getFrontCorners();
        } else {
            corners = card.getBackCorners();
        }

        structure[y + 2][x - 4] = '┌';
        structure[y + 2][x - 3] = '─';
        structure[y + 2][x - 2] = '┐';
        structure[y + 2][x - 1] = '─';
        structure[y + 2][x] = '─';
        structure[y + 2][x + 1] = '─';
        structure[y + 2][x + 2] = '┌';
        structure[y + 2][x + 3] = '─';
        structure[y + 2][x + 4] = '┐';

        structure[y + 1][x - 4] = '│';
        structure[y + 1][x - 3] = corners.get(0).charAt(0);
        structure[y + 1][x - 2] = '|';
        structure[y + 1][x - 1] = ' ';
        if (card instanceof InitialCard && card.getFrontCenterResources().size() > 1)
            structure[y + 1][x] = card.getFrontCenterResources().get(0).charAt(0);
        else
            structure[y + 1][x] = ' ';
        // else if card has points -> print points
        structure[y + 1][x + 1] = ' ';
        structure[y + 1][x + 2] = '│';
        structure[y + 1][x + 3] = corners.get(1).charAt(0);
        structure[y + 1][x + 4] = '│';

        structure[y][x - 4] = '|';
        structure[y][x - 3] = '-';
        structure[y][x - 2] = '|';
        if (card instanceof GoldCard || card instanceof ResourceCard) {
            structure[y][x - 1] = card.getIdCard().charAt(0);
            structure[y][x] = card.getIdCard().charAt(1);
            structure[y][x + 1] = card.getIdCard().charAt(2);
        } else {
            structure[y][x - 1] = ' ';
            if (card.getFrontCenterResources().size() == 3)
                structure[y][x] = card.getFrontCenterResources().get(1).charAt(0);
            else if (card.getFrontCenterResources().size() == 1)
                structure[y][x] = card.getFrontCenterResources().get(0).charAt(0);
            else
                structure[y][x] = ' ';
            structure[y][x + 1] = ' ';
        }
        structure[y][x + 2] = '|';
        structure[y][x + 3] = '-';
        structure[y][x + 4] = '|';

        structure[y - 1][x - 4] = '│';
        structure[y - 1][x - 3] = corners.get(2).charAt(0);
        structure[y - 1][x - 2] = '|';
        structure[y - 1][x - 1] = ' ';
        if (card instanceof InitialCard && card.getFrontCenterResources().size() == 3)
            structure[y - 1][x] = card.getFrontCenterResources().get(2).charAt(0);
        else if (card instanceof InitialCard && card.getFrontCenterResources().size() == 2)
            structure[y - 1][x] = card.getFrontCenterResources().get(1).charAt(0);
        else
            structure[y - 1][x] = ' ';
        structure[y - 1][x + 1] = ' ';
        structure[y - 1][x + 2] = '│';
        structure[y - 1][x + 3] = corners.get(3).charAt(0);
        structure[y - 1][x + 4] = '│';

        structure[y - 2][x - 4] = '└';
        structure[y - 2][x - 3] = '─';
        structure[y - 2][x - 2] = '┘';
        structure[y - 2][x - 1] = '─';
        structure[y - 2][x] = '─';
        structure[y - 2][x + 1] = '─';
        structure[y - 2][x + 2] = '└';
        structure[y - 2][x + 3] = '─';
        structure[y - 2][x + 4] = '┘';
    }
}

/*
 * ┌─┐───┌─┐
 * │N| 2 |I│
 * │─|R01|─|
 * │V| |A│
 * └─┘───└─┘
 */