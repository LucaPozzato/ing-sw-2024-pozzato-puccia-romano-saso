package it.polimi.ingsw.codexnaturalis.model.game.components.structure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.ingsw.codexnaturalis.model.enumerations.Objects;
import it.polimi.ingsw.codexnaturalis.model.enumerations.Resource;
import it.polimi.ingsw.codexnaturalis.model.exceptions.IllegalCommandException;
import it.polimi.ingsw.codexnaturalis.model.game.Printer;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.Card;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.GoldCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.InitialCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ObjectiveCard;
import it.polimi.ingsw.codexnaturalis.model.game.components.cards.ResourceCard;

import static java.lang.Math.abs;

public class Structure {
    // TODO: delete regionmatches -> code hard to read
    // TODO: add message to IllegalCommandExceptions
    private List<Card> placedCards;
    private Map<Card, Triplet<Integer, Boolean, Boolean>> cardToCoordinate; // <Card , <Coordinates, Side, Visited> >
    private Map<Integer, Triplet<Card, Boolean, Boolean>> coordinateToCard; // <Coordinates , <Card, Side, Visited> >
    private char[][] visualStructure = new char[174][506];
    private Map<String, Integer> visibleSymbols;
    private Card[][] cardMatrix = new Card[80][80];
    private int satisfiedPatterns;

    public Structure() {
        this.placedCards = new ArrayList<>();
        this.cardToCoordinate = new HashMap<>();
        this.coordinateToCard = new HashMap<>();
        this.visibleSymbols = new HashMap<>(
                Map.of("VEGETABLE", 0, "ANIMAL", 0, "INSECT", 0, "SHROOM", 0, "INK", 0, "SCROLL", 0,
                        "FEATHER", 0, "EMPTY", 0, "NULL", 0));
        this.satisfiedPatterns = 0;
    }

    public Map<Card, Triplet<Integer, Boolean, Boolean>> getCardToCoordinate() {
        return cardToCoordinate;
        /*
         * TO PRINT:
         * for(Card card : structure.getCardToCoordinate().keySet()){
         * System.out.println( "id card: " + card.getIdCard() + "coordinate: " +
         * structure.getCardToCoordinate().get(card).getFirst());
         * }
         */
    }

    public Map<Integer, Triplet<Card, Boolean, Boolean>> getCoordinateToCard() {
        return coordinateToCard;
    }

    public Card[][] getCardMatrix() {
        return cardMatrix;
    }

    public String getVisibleResources() {
        String visibleResources = "";
        for (Resource resource : Resource.values()) {
            if (!(resource.name().equals("EMPTY")) && !(resource.name().equals("NULL")))
                visibleResources += resource.name() + ": " + visibleSymbols.get(resource.name()) + "\n";
        }
        visibleResources = visibleResources.substring(0, visibleResources.length() - 1);
        return visibleResources;
    }

    public String getVisibleObjects() {
        String visibleObjects = "";
        for (Objects resource : Objects.values()) {
            visibleObjects += resource.name() + ": " + visibleSymbols.get(resource.name()) + "\n";
        }
        visibleObjects = visibleObjects.substring(0, visibleObjects.length() - 1);
        return visibleObjects;
    }

    public Map<String, Integer> getvisibleSymbols(){
        return visibleSymbols;
    }

    public List<Card> getPlacedCards() {
        return placedCards;
    }

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

    public void increaseSatisfiedPatterns(){
        this.satisfiedPatterns++;
    }
    public int getSatisfiedPatterns(){
        return satisfiedPatterns;
    }

    private Boolean isPlaceable(Card father, Card card, Integer coordinate, String position, Boolean frontUp)
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
                if (coordinateToCard.get(coordinate + 99).getFirst().getFrontCorners().get(0).equals("NULL"))
                    throw new IllegalCommandException("Card cannot be placed on null corner");
            } else if (coordinateToCard.get(coordinate + 99).getFirst().getBackCorners().get(0).equals("NULL"))
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

    public void placeCard(Card father, Card card, String position, Boolean frontUp) throws IllegalCommandException {
        // Calculates coordinates of new card
        Integer destinationCoord = calcCoordinate(father, position);
        // Checks if the card can be placed and places it
        isPlaceable(father, card, destinationCoord, position, frontUp);

        cardToCoordinate.put(card, new Triplet<>(destinationCoord, frontUp, false));
        coordinateToCard.put(destinationCoord, new Triplet<>(card, frontUp, false));

        placedCards.add(card);
        addCardToVisual(card, frontUp);
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

    private void calcVisibleSymbols() {
        int minX = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 253 + (i / 100 - 40) * 6)
                .min().getAsInt();
        int minY = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 87 - (i % 100 - 40) * 2)
                .min().getAsInt();
        int maxX = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 253 + (i / 100 - 40) * 6)
                .max().getAsInt();
        int maxY = coordinateToCard.keySet().stream().mapToInt(Integer::intValue).map(i -> 87 - (i % 100 - 40) * 2)
                .max().getAsInt();

        // Resets visible symbols
        for (String symbol : visibleSymbols.keySet()) {
            visibleSymbols.put(symbol, 0);
        }

        // Updates visible symbols
        for (int i = minY - 2; i <= maxY + 2; i++) {
            for (int j = minX - 4; j <= maxX + 4; j++) {
                switch (visualStructure[i][j]) {
                    case 'A':
                        visibleSymbols.put("ANIMAL", visibleSymbols.get("ANIMAL") + 1);
                        break;
                    case 'V':
                        visibleSymbols.put("VEGETABLE", visibleSymbols.get("VEGETABLE") + 1);
                        break;
                    case 'I':
                        visibleSymbols.put("INSECT", visibleSymbols.get("INSECT") + 1);
                        break;
                    case 'S':
                        visibleSymbols.put("SHROOM", visibleSymbols.get("SHROOM") + 1);
                        break;
                    case 's':
                        visibleSymbols.put("SCROLL", visibleSymbols.get("SCROLL") + 1);
                        break;
                    case 'i':
                        visibleSymbols.put("INK", visibleSymbols.get("INK") + 1);
                        break;
                    case 'f':
                        visibleSymbols.put("FEATHER", visibleSymbols.get("FEATHER") + 1);
                        break;
                    case 'N':
                        visibleSymbols.put("NULL", visibleSymbols.get("NULL") + 1);
                        break;
                    case 'E':
                        visibleSymbols.put("EMPTY", visibleSymbols.get("EMPTY") + 1);
                        break;
                    default:
                        break;
                }
            }
        }
    }

    private void addToCardMatrix(Card card) {
        int x = cardToCoordinate.get(card).getFirst() / 100;
        int y = 40 + (cardToCoordinate.get(card).getFirst() % 100 - 40);
        cardMatrix[x][y] = card;
    }

    /**
     * This method's aim is to provide a simplified search space for SearchXPattern's methods.
     * @param coordinateToCard to extract the coordinate of the placed card
     * @return max distance from the centre of the matrux
     * @throws IllegalCommandException ex
     */
    public int getRadius(Map<Integer, Triplet<Card, Boolean, Boolean>> coordinateToCard) throws IllegalCommandException{
        int radius = 0;
        for(Integer key : coordinateToCard.keySet()){
            int x = key/100;
            int y = key%100;
            int x_distance = abs(x - 40);
            int y_distance = abs(y - 40);
            if(x_distance >= radius) radius = x_distance;
            else if (y_distance >= radius) radius = y_distance;
        }
        return radius;
    }

    /**
     * This method is used to test purposes after filling the structure with some values. It will print a [2*radius][2*radius] matrix
     * @param fullMatrix the full [80][80] matrix
     * @param radius radius computed in getRadius
     */
    public void printReducedMatrix(Card[][] fullMatrix, Integer radius){
        for (int i = 40 + radius ; i >= 40 - radius; i--) {
            for (int j = 40 - radius ; j <= 40 + radius; j++) {
                if (fullMatrix[j][i] == null) {
                    System.out.print("[---]");
                } else {
                    System.out.print("[" + fullMatrix[j][i].getIdCard() + "]");
                }
            }
            System.out.print("\n");
        }
    }

    private void addCardToVisual(Card card, Boolean frontUp) throws IllegalCommandException {
        int x = cardToCoordinate.get(card).getFirst() / 100;
        int y = cardToCoordinate.get(card).getFirst() % 100;

        // Center of the matrix + x offset (6 pixels per card)
        x = 253 + ((x - 40) * 6);
        // Inverting y coordinate to match the matrix structure, center of the matrix +
        // y offset (2 pixels per card)
        y = 87 - ((y - 40) * 2);

        visualStructure = card.drawVisual(visualStructure, x, y, frontUp);
    }

    public String draw() throws IllegalCommandException {
        return new Printer().printStructure(visualStructure, coordinateToCard);
    }
}