package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

import static java.lang.Math.abs;

import java.io.Serial;
import java.io.Serializable;
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
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;
import javafx.util.Pair;

/**
 * Represents the structure where cards are placed in the game.
 * Provides methods for managing and interacting with the structure,
 * including placing cards, calculating points, and checking placements.
 */
public class Structure implements Serializable {
    @Serial
    private static final long serialVersionUID = 109283746501928L;
    private List<Pair<Card, Boolean>> placedCards;
    private Map<Card, Triplet<Integer, Boolean, Boolean>> cardToCoordinate; // <Card , <Coordinates, Side, Visited> >
    private Map<Integer, Triplet<Card, Boolean, Boolean>> coordinateToCard; // <Coordinates , <Card, Side, Visited> >
    private Map<String, Integer> visibleSymbols;
    private Card[][] cardMatrix = new Card[80][80];
    private int satisfiedObj;

    /**
     * Constructs a new Structure with initial settings.
     * Initializes lists, maps, and counters.
     */
    public Structure() {
        this.placedCards = new ArrayList<>();
        this.cardToCoordinate = new HashMap<>();
        this.coordinateToCard = new HashMap<>();
        this.visibleSymbols = new HashMap<>(
                Map.of("VEGETABLE", 0, "ANIMAL", 0, "INSECT", 0, "SHROOM", 0, "INK", 0, "SCROLL", 0,
                        "FEATHER", 0, "EMPTY", 0, "NULL", 0));
        this.satisfiedObj = 0;
    }

    public Map<Card, Triplet<Integer, Boolean, Boolean>> getCardToCoordinate() {
        return cardToCoordinate;
    }

    public Map<Integer, Triplet<Card, Boolean, Boolean>> getCoordinateToCard() {
        return coordinateToCard;
    }

    public Card[][] getCardMatrix() {
        return cardMatrix;
    }

    public void setSatisfiedObj() {
        this.satisfiedObj++;
    }

    public int getSatisfiedObj() {
        return satisfiedObj;
    }

    public Map<String, Integer> getvisibleSymbols() {
        return visibleSymbols;
    }

    public String getVisibleResources() {
        StringBuilder visibleResources = new StringBuilder();
        for (Resource resource : Resource.values()) {
            if (!(resource.name().equals("EMPTY")) && !(resource.name().equals("NULL")))
                visibleResources.append(resource.name()).append(": ").append(visibleSymbols.get(resource.name())).append("\n");
        }
        visibleResources = new StringBuilder(visibleResources.substring(0, visibleResources.length() - 1));
        return visibleResources.toString();
    }

    public String getVisibleObjects() {
        StringBuilder visibleObjects = new StringBuilder();
        for (Objects resource : Objects.values()) {
            visibleObjects.append(resource.name()).append(": ").append(visibleSymbols.get(resource.name())).append("\n");
        }
        visibleObjects = new StringBuilder(visibleObjects.substring(0, visibleObjects.length() - 1));
        return visibleObjects.toString();
    }

    public List<Pair<Card, Boolean>> getPlacedCards() {
        return placedCards;
    }

    /**
     * This method is used to calculate the points that a card placed on the
     * structure will give. It checks if the card is front up or down and if it is
     * a gold or resource card. If the card is front up, it will check the points
     * type and return the points accordingly.
     *
     * @param placed  the card to be placed
     * @param frontUp the side of the card that will be visible (front side up ->
     *                true, front side down -> false)
     * @return the points that the card will give
     * @throws IllegalCommandException if the card is not a gold or resource card
     */
    public int getPointsFromPlayableCard(Card placed, Boolean frontUp) throws IllegalCommandException {
        if (frontUp == null)
            throw new IllegalArgumentException("FrontUp cannot be null");
        if (placed == null)
            throw new IllegalArgumentException("Card cannot be null");

        if (placed instanceof InitialCard || placed instanceof ObjectiveCard)
            throw new IllegalArgumentException("Passed neither gold nor resource card");

        if (frontUp && placed.getPoints() > 0) {
            if (placed instanceof ResourceCard) {
                return 1;
            } else if (placed instanceof GoldCard) {
                if (placed.getPointsType().equals("NULL")) {
                    return placed.getPoints();
                } else if (!placed.getPointsType().equals("ANGLE")) {
                    return visibleSymbols.get(placed.getPointsType());
                } else if (placed.getPointsType().equals("ANGLE")) {
                    int coveredCorners = 0;
                    int placedCoord = cardToCoordinate.get(placed).getFirst();
                    if (coordinateToCard.get(placedCoord + 99) != null)
                        coveredCorners++;
                    if (coordinateToCard.get(placedCoord - 99) != null)
                        coveredCorners++;
                    if (coordinateToCard.get(placedCoord + 101) != null)
                        coveredCorners++;
                    if (coordinateToCard.get(placedCoord - 101) != null)
                        coveredCorners++;
                    return coveredCorners * 2;
                }
            }
        }
        return 0;
    }


    /**
     * This method acts differently according to the type of card the user want to
     * place. <br>
     * If the card is an initial one isPlaceable return true only in there's not
     * another initial card placed on the structure. <br>
     * Else it sequentially performs these actions: Checks if the father card is
     * present <br>
     * If father card is not placed, the new card cannot be placed <br>
     * If father card is not placed, the new card cannot be placed <br>
     * Checks if the card is already placed <br>
     * Checks if another card is already placed in that coordinate <br>
     * Checks if the placed card does not cover one of the uncoverable corners of
     * neighbour cards <br>
     * If the placed card is a Gold one checks if the player's structure satisfies
     * card's requirement <br>
     *
     * @param father     the card under the placed one
     * @param card       the card user want to place
     * @param coordinate the coordinate on the matrix where the card is placed
     *                   computed in the placeCard method according to the position
     *                   string passed
     * @param frontUp    the side choose for the placement
     * @return true is the card is placeable, exception otherwise. The method never
     * returns false.
     * @throws IllegalCommandException thrown when a card is not placeable. A proper
     *                                 description message is linked to the
     *                                 exception.
     */
    // TODO: consider to set as private this method, made public for test purposes
    public Boolean isPlaceable(Card father, Card card, Integer coordinate, Boolean frontUp)
            throws IllegalCommandException {
        // checks if initial card is placeable
        if (card instanceof InitialCard) {
            if (coordinateToCard.containsKey(4040)) {
                throw new IllegalCommandException("Initial card already placed");
            } else
                return true;
        }

        // Checks if the father card is present
        if (father == null)
            throw new IllegalCommandException("Bottom card cannot be null");

        // If father card is not placed, the new card cannot be placed
        if (!cardToCoordinate.containsKey(father))
            throw new IllegalCommandException("Bottom card is not placed");

        // Checks if the card is already placed
        if (cardToCoordinate.containsKey(card))
            throw new IllegalCommandException("Card is already placed");

        // Checks if another card is already placed in that coordinate
        if (coordinateToCard.containsKey(coordinate))
            throw new IllegalCommandException("Another card is already placed in that position");

        // Checks if top left card exists and does not have null in BR corner
        if (coordinateToCard.containsKey(coordinate - 99)) {
            if (coordinateToCard.get(coordinate - 99).getSide()) {
                if (coordinateToCard.get(coordinate - 99).getFirst().getFrontCorners().get(3).equals("NULL"))
                    throw new IllegalCommandException("Card cannot be placed on null corner");
            } else if (coordinateToCard.get(coordinate - 99).getFirst().getBackCorners().get(3).equals("NULL"))
                throw new IllegalCommandException("Card cannot be placed on null corner");
        }
        // Checks if top right card exists and does not have null in BL corner
        if (coordinateToCard.containsKey(coordinate + 101)) {
            if (coordinateToCard.get(coordinate + 101).getSide()) {
                if (coordinateToCard.get(coordinate + 101).getFirst().getFrontCorners().get(2).equals("NULL"))
                    throw new IllegalCommandException("Card cannot be placed on null corner");
            } else if (coordinateToCard.get(coordinate + 101).getFirst().getBackCorners().get(2).equals("NULL"))
                throw new IllegalCommandException("Card cannot be placed on null corner");
        }
        // Checks if bottom left card exists and does not have null in TR corner
        if (coordinateToCard.containsKey(coordinate - 101)) {
            if (coordinateToCard.get(coordinate - 101).getSide()) {
                if (coordinateToCard.get(coordinate - 101).getFirst().getFrontCorners().get(1).equals("NULL"))
                    throw new IllegalCommandException("Card cannot be placed on null corner");
            } else if (coordinateToCard.get(coordinate - 101).getFirst().getBackCorners().get(1).equals("NULL"))
                throw new IllegalCommandException("Card cannot be placed on null corner");
        }
        // Checks if bottom right card exists and does not have null in TL corner
        if (coordinateToCard.containsKey(coordinate + 99)) {
            if (coordinateToCard.get(coordinate + 99).getSide()) {
                if (coordinateToCard.get(coordinate + 99).getFirst().getFrontCorners().getFirst().equals("NULL"))
                    throw new IllegalCommandException("Card cannot be placed on null corner");
            } else if (coordinateToCard.get(coordinate + 99).getFirst().getBackCorners().getFirst().equals("NULL"))
                throw new IllegalCommandException("Card cannot be placed on null corner");
        }

        // Checks if the gold card has the requirements to be placed
        if (card instanceof GoldCard && frontUp) {
            for (Map.Entry<String, Integer> entry : card.getRequirements().entrySet()) {
                if (visibleSymbols.get(entry.getKey()) < entry.getValue())
                    throw new IllegalCommandException("Gold card requirements not met");
            }
        }
        return true;
    }

    /**
     * This method is used to place a card on the structure. It checks if the card
     * can be placed and then places it.
     *
     * @param father   the card on which the new card will be placed
     * @param card     the card to be placed
     * @param position the position of the new card relative to the father card (TL,
     *                 TR, BL, BR)
     * @param frontUp  the side of the card that will be visible (front side up ->
     *                 true, front side down -> false)
     * @throws IllegalCommandException if the card cannot be placed
     */
    public void placeCard(Card father, Card card, String position, Boolean frontUp) throws IllegalCommandException {
        // Calculates coordinates of new card
        Integer destinationCoord = calcCoordinate(father, position);
        // Checks if the card can be placed and places it
        isPlaceable(father, card, destinationCoord, frontUp);

        cardToCoordinate.put(card, new Triplet<>(destinationCoord, frontUp, false));
        coordinateToCard.put(destinationCoord, new Triplet<>(card, frontUp, false));

        placedCards.add(new Pair<Card, Boolean>(card, frontUp));
        addToCardMatrix(card);
        calcVisibleSymbols();
    }

    private Integer calcCoordinate(Card father, String position) throws IllegalArgumentException {
        if (father == null)
            return 4040;

        if (!cardToCoordinate.containsKey(father))
            throw new IllegalArgumentException("Bottom card is not placed");

        Integer fatherCoordinate = cardToCoordinate.get(father).getFirst();

        // Important: these coordinates are correct in the Euclidean plane, but not when
        // thinking about the structure as a matrix. A matrix has the origin in the top
        // left corner, while the Euclidean plane has the origin in the bottom left
        // corner. This means that the y coordinate is inverted.

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
                throw new IllegalArgumentException("Invalid position"); // -> maybe function that calls calcCoordinates
                // checks for suitable arguments
        }
    }

    /**
     * Updates the visibility of symbols on the structure based on the placement of
     * the last card.
     *
     * @throws IllegalCommandException if there is an issue with the visibility
     *                                 calculation
     *                                 based on the structure's current state
     */

    private void calcVisibleSymbols() throws IllegalCommandException {
        Card card = placedCards.getLast().getKey();
        Boolean frontSideUp = placedCards.getLast().getValue();
        Integer coordinate = cardToCoordinate.get(card).getFirst();
        String symbol = "";

        if (frontSideUp) {
            if (card instanceof InitialCard) {
                for (String s : card.getFrontCenterResources()) {
                    visibleSymbols.put(s, visibleSymbols.get(s) + 1);
                }
            }
            for (String s : card.getFrontCorners()) {
                visibleSymbols.put(s, visibleSymbols.get(s) + 1);
            }
        } else {
            if (!(card instanceof InitialCard))
                visibleSymbols.put(card.getSymbol(), visibleSymbols.get(card.getSymbol()) + 1);
            for (String s : card.getBackCorners()) {
                visibleSymbols.put(s, visibleSymbols.get(s) + 1);
            }
        }

        // Checks if top left card exists and if so, removes symbol in BR corner
        if (coordinateToCard.containsKey(coordinate - 99)) {
            if (coordinateToCard.get(coordinate - 99).getSide())
                symbol = coordinateToCard.get(coordinate - 99).getFirst().getFrontCorners().get(3);
            else
                symbol = coordinateToCard.get(coordinate - 99).getFirst().getBackCorners().get(3);

            visibleSymbols.put(symbol, visibleSymbols.get(symbol) - 1);
            symbol = "";
        }
        // Checks if top right card exists and if so, removes symbol in BL corner
        if (coordinateToCard.containsKey(coordinate + 101)) {
            if (coordinateToCard.get(coordinate + 101).getSide())
                symbol = coordinateToCard.get(coordinate + 101).getFirst().getFrontCorners().get(2);
            else
                symbol = coordinateToCard.get(coordinate + 101).getFirst().getBackCorners().get(2);

            visibleSymbols.put(symbol, visibleSymbols.get(symbol) - 1);
            symbol = "";
        }
        // Checks if bottom left card exists and if so, removes symbol in TR corner
        if (coordinateToCard.containsKey(coordinate - 101)) {
            if (coordinateToCard.get(coordinate - 101).getSide())
                symbol = coordinateToCard.get(coordinate - 101).getFirst().getFrontCorners().get(1);
            else
                symbol = coordinateToCard.get(coordinate - 101).getFirst().getBackCorners().get(1);

            visibleSymbols.put(symbol, visibleSymbols.get(symbol) - 1);
            symbol = "";
        }
        // Checks if bottom right card exists and if so, removes symbol in TL corner
        if (coordinateToCard.containsKey(coordinate + 99)) {
            if (coordinateToCard.get(coordinate + 99).getSide())
                symbol = coordinateToCard.get(coordinate + 99).getFirst().getFrontCorners().get(0);
            else
                symbol = coordinateToCard.get(coordinate + 99).getFirst().getBackCorners().get(0);

            visibleSymbols.put(symbol, visibleSymbols.get(symbol) - 1);
            symbol = "";
        }
    }

    private void addToCardMatrix(Card card) {
        int x = cardToCoordinate.get(card).getFirst() / 100;
        int y = 40 + (cardToCoordinate.get(card).getFirst() % 100 - 40);
        cardMatrix[x][y] = card;
    }

    /**
     * This method's aim is to provide a simplified search space for
     * SearchXPattern's methods.
     *
     * @param coordinateToCard to extract the coordinate of the placed card
     * @return max distance from the centre of the matrux
     * @throws IllegalCommandException ex
     */
    public int getRadius(Map<Integer, Triplet<Card, Boolean, Boolean>> coordinateToCard)
            throws IllegalCommandException {
        int radius = 0;
        for (Integer key : coordinateToCard.keySet()) {
            int x = key / 100;
            int y = key % 100;
            int x_distance = abs(x - 40);
            int y_distance = abs(y - 40);
            if (x_distance >= radius)
                radius = x_distance;
            else if (y_distance >= radius)
                radius = y_distance;
        }
        return radius;
    }

    /**
     * This method is used to test purposes after filling the structure with some
     * values. It will print a [2*radius][2*radius] matrix
     *
     * @param fullMatrix the full [80][80] matrix
     * @param radius     radius computed in getRadius
     */
    public void printReducedMatrix(Card[][] fullMatrix, Integer radius) {
        for (int i = 40 + radius; i >= 40 - radius; i--) {
            for (int j = 40 - radius; j <= 40 + radius; j++) {
                if (fullMatrix[j][i] == null) {
                    System.out.print("[---]");
                } else {
                    System.out.print("[" + fullMatrix[j][i].getIdCard() + "]");
                }
            }
            System.out.print("\n");
        }
    }
}